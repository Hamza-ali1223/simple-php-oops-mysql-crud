package com.oopscrud;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    String list(Model model) {
        model.addAttribute("users", service.findAll());
        return "users/list";
    }

    @GetMapping("/users/new")
    String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/form";
    }

    @PostMapping("/users")
    String create(@Valid UserForm form, BindingResult errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "users/form";
        }
        service.create(form.toUser());
        flash.addFlashAttribute("successMessage", "User created.");
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/edit")
    String editForm(@PathVariable long id, Model model) {
        model.addAttribute("userForm", UserForm.from(service.getExisting(id)));
        return "users/form";
    }

    @PostMapping("/users/{id}")
    String update(@PathVariable long id, @Valid UserForm form, BindingResult errors, RedirectAttributes flash) {
        form.setId(id);
        if (errors.hasErrors()) {
            return "users/form";
        }
        service.update(form.toUser());
        flash.addFlashAttribute("successMessage", "User updated.");
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/delete")
    String delete(@PathVariable long id, RedirectAttributes flash) {
        service.delete(id);
        flash.addFlashAttribute("successMessage", "User deleted.");
        return "redirect:/users";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound() {
        return "error/not-found";
    }
}
