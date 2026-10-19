# PHP to Java Migration Delta Analysis

## 4.1 Title and purpose

This document compares the current PHP/MySQL user CRUD application with
the agreed Java Spring Boot MVC + Thymeleaf + SQLite target. It
identifies the behavior to preserve, implementation changes, new
components required, and existing components that may be retired after
acceptance.

This is a planning document. It is not Java implementation work. No
application code, database, dependencies, or configuration are changed
by this analysis.

The target SQLite database will start empty. Existing MySQL records are
intentionally not migrated. The target will preserve the logical users
schema and the main user CRUD functionality.

> **Evidence note:** This draft is based on the migration issue's stated
> baseline and file list. Before committing, verify each current-system
> observation against the repository source files. The repository itself
> was not included with the task text provided for this draft.

## 4.2 Current-system baseline

The current application is described as a small PHP/MySQL user CRUD
application.

  -----------------------------------------------------------------------
  Current item            Current implementation  Source to verify
  ----------------------- ----------------------- -----------------------
  List and delete page    `index.php`             `index.php`

  Add and edit page       `manage-users.php`      `manage-users.php`

  CRUD query methods      `query.php`             `query.php`

  MySQLi connection       `database.php`          `database.php`

  Manual example          `test.php`              `test.php`

  Database                MySQL database          `database.php`,
                          `oops_crud`             `README.md`

  Main table              `users`                 SQL statements in PHP
                                                  files and README

  User fields             `id`, `name`, `email`,  SQL statements and form
                          `mobile`                fields in PHP files
  -----------------------------------------------------------------------

### Current user-visible flows

-   **View users:** The user opens `index.php` to view the user list.
-   **Add user:** The user opens `manage-users.php`, enters user
    details, and submits the form to create a record.
-   **Edit user:** The user selects a user to edit, opens
    `manage-users.php` with the relevant user identifier, changes the
    details, and submits the form.
-   **Delete user:** The user initiates deletion from the list page. The
    exact request method and confirmation behavior must be verified in
    `index.php`.

The precise form fields, validation rules, redirects, query behavior,
and error handling must be confirmed from the PHP source before this
baseline is approved. The current system in this section is the PHP
implementation only; the Java target is described separately below.

## 4.3 Target-system baseline

The agreed target is a browser-based, server-rendered CRUD application
using the following technologies and decisions:

-   **Language:** Java
-   **Application framework:** Spring Boot
-   **Web approach:** Spring MVC
-   **View/template engine:** Thymeleaf
-   **Build tool:** Maven
-   **Database:** SQLite
-   **Schema:** Keep the same logical `users` schema and fields: `id`,
    `name`, `email`, and `mobile`
-   **Initial data:** Start with an empty SQLite database; do not
    migrate existing MySQL records
-   **Main functionality:** Preserve user list, add, edit, and delete
    operations

The target remains a browser-based server-rendered application. This
issue does not select a frontend framework, introduce REST APIs, or
change the agreed technology choices.

## 4.4 Preserve / Change / Add / Remove matrix

  --------------------------------------------------------------------------------------
  Category          Current state        Target state             Reason / migration
                                                                  note
  ----------------- -------------------- ------------------------ ----------------------
  Preserve          Users can be listed  Users can be listed in   Maintain the existing
                                         the browser              core user-management
                                                                  capability.

  Preserve          Users can be created Users can be created     Preserve create
                                         through a                behavior and required
                                         server-rendered form     user attributes.

  Preserve          Existing users can   Users can be edited      Preserve update
                    be edited            through a                capability.
                                         server-rendered form     

  Preserve          Users can be deleted Users can be deleted     Preserve delete
                                         through the target       capability while using
                                         application              safe request semantics
                                                                  in the Java
                                                                  implementation.

  Preserve          Logical fields are   Keep the same logical    Maintain the logical
                    `id`, `name`,        user attributes          schema; physical SQL
                    `email`, `mobile`                             types and generated-ID
                                                                  syntax may differ.

  Preserve          Browser-based        Spring MVC with          Keep the same broad
                    server-rendered user Thymeleaf                interaction model
                    interface            server-rendered pages    without introducing a
                                                                  frontend framework.

  Preserve          Successful create,   Return to the user list  Verify the exact
                    update, or delete    after successful create, current redirects in
                    returns the user to  update, or delete        PHP; implement the
                    the list, as                                  agreed list-return
                    required by the                               behavior in Java.
                    target specification                          

  Change            PHP pages handle     Spring MVC controllers   Separate request
                    page and request     handle requests and      handling from page
                    behavior             Thymeleaf renders views  templates in the Java
                                                                  application.

  Change            `database.php` uses  Use Spring-managed       Replace PHP-specific
                    a MySQLi connection  access to SQLite         connection handling
                                                                  with Java-compatible
                                                                  data access.

  Change            Data is stored in    Data is stored in SQLite Follow the agreed
                    MySQL database                                target database; do
                    `oops_crud`                                   not transfer existing
                                                                  records.

  Change            Generic PHP CRUD     Java entity/model,       Use the planned Java
                    query methods        repository, service, and application structure
                                         controller layers        and responsibilities.

  Change            Database settings    Externalized Java        Avoid embedding
                    are described as     application              environment-specific
                    hard-coded in the    configuration            settings in Java
                    current application;                          source.
                    verify in                                     
                    `database.php`                                

  Change            PHP forms process    Spring MVC form binding  Bind form data to Java
                    submitted values                              model objects and
                                                                  handle validation
                                                                  errors.

  Change            Generated IDs follow Use SQLite-compatible    Account for
                    MySQL-specific       generated-ID behavior    differences in
                    behavior                                      generated-ID syntax
                                                                  and retrieval.

  Change            Existing MySQL       Fresh, empty SQLite      This is an explicit
                    database may contain database                 target decision; no
                    records                                       existing MySQL records
                                                                  are migrated.

  Add               No Maven-based Java  Maven project structure  Provides repeatable
                    project is                                    dependency management
                    identified in the                             and build commands.
                    stated baseline                               

  Add               No Spring Boot entry Spring Boot application  Allows the Java
                    point is identified  entry point              application to start
                    in the stated                                 through Spring Boot.
                    baseline                                      

  Add               PHP data handling    Java `User` entity/model Represents the logical
                    represents users                              user data in the Java
                                                                  application.

  Add               Existing connection  SQLite application       Configures the target
                    targets MySQL        configuration            to use the SQLite
                                                                  database file.

  Add               CRUD SQL methods are Java                     Encapsulates database
                    in PHP               data-access/repository   operations.
                                         layer                    

  Add               CRUD logic is in the Java service layer       Provides a place for
                    existing PHP                                  application/business
                    implementation                                logic.

  Add               PHP files handle web Spring MVC controllers   Map browser requests
                    requests                                      to application
                                                                  operations and views.

  Add               PHP pages render the Thymeleaf views          Render server-side
                    current interface                             HTML for list and form
                                                                  pages.

  Add               Current validation   Server-side validation   Validate submitted
                    is described as                               values on the server;
                    limited; verify the                           exact rules remain an
                    exact rules                                   open question where
                                                                  not agreed.

  Add               Missing or invalid   Missing-user error       Define predictable
                    user handling must   handling                 behavior when an
                    be reviewed                                   edit/delete identifier
                                                                  does not refer to a
                                                                  valid user.

  Add               No automated test    Automated CRUD and       Provide repeatable
                    suite is identified  validation tests         checks for core
                    in the stated                                 behavior and
                    baseline                                      validation.

  Add               Existing setup       Repeatable Java setup    Explain how to
                    documentation        and run documentation    configure, build, run,
                    describes the PHP                             and demonstrate the
                    application                                   Java target.

  Remove            PHP runtime          Retire PHP runtime       Do not remove or
                    implementation       implementation only      modify PHP files in
                    provides current     after Java acceptance    this analysis issue.
                    behavior                                      

  Remove            `database.php`       Retire `database.php`    It is specific to the
                    manages the MySQLi   only after Java          PHP implementation and
                    connection           acceptance               is not part of the
                                                                  Java target.

  Remove            `query.php` contains Retire `query.php` only  Its responsibilities
                    PHP CRUD query       after Java acceptance    will be replaced by
                    methods                                       Java data-access
                                                                  layers.

  Remove            `index.php` provides Retire `index.php` only  Its user-facing
                    the list/delete page after Java acceptance    behavior will be
                                                                  provided by Spring MVC
                                                                  and Thymeleaf.

  Remove            `manage-users.php`   Retire                   Its user-facing
                    provides add/edit    `manage-users.php` only  behavior will be
                    pages                after Java acceptance    provided by Spring MVC
                                                                  and Thymeleaf.

  Remove            Target is currently  Retire the MySQL/XAMPP   The Java target uses
                    described as         runtime requirement for  SQLite; confirm any
                    requiring the        the target application   repository-wide
                    PHP/MySQL runtime    after acceptance         tooling dependencies
                                                                  separately.

  Remove            `test.php` is        Retire or replace the    Confirm its contents
                    described as an      obsolete manual example  first; it must not be
                    inconsistent manual  after acceptance         deleted in this issue.
                    example referring to                          
                    employee data                                 

  Remove            PHP-specific setup   Retire PHP-specific      Keep instructions for
                    instructions apply   setup instructions after the existing
                    to the current       Java replacement         application until the
                    implementation       documentation exists     Java setup
                                                                  documentation is
                                                                  available and
                                                                  accepted.
  --------------------------------------------------------------------------------------

**Important:** "Remove" means retire only after the Java application has
been accepted. It does not authorize deleting files in this issue.

## 4.5 Risks and technical-debt findings

The items below are observations and risks to verify, not changes to
implement in this issue. The handling category identifies where each
item should be addressed.

  ---------------------------------------------------------------------------
  Risk / observation          Why it matters          Handling
  --------------------------- ----------------------- -----------------------
  SQL query construction may  Unsafe query            Handle during Java
  use concatenated or         construction can expose implementation
  otherwise unsafe input      the application to SQL  
                              injection. Inspect the  
                              actual query methods    
                              before confirming the   
                              precise pattern.        

  Database                    Credentials in source   Handle during Java
  credentials/configuration   are difficult to manage implementation
  may be hard-coded           safely across           
                              environments. Verify    
                              the current             
                              configuration in        
                              `database.php`.         

  Server-side validation      Missing validation can  Handle during Java
  appears limited in the      allow incomplete or     implementation
  stated baseline             invalid values to be    
                              stored. Exact current   
                              rules must be checked   
                              in the PHP forms and    
                              query logic.            

  Output escaping has not     Unescaped               Verify during
  been reviewed               user-controlled values  acceptance testing
                              may create cross-site   
                              scripting risk when     
                              rendered in HTML.       

  Delete may be initiated     GET requests should not Handle during Java
  with GET                    perform state-changing  implementation
                              actions; links can be   
                              triggered               
                              unintentionally by      
                              crawlers or previews.   
                              Confirm the actual      
                              request in `index.php`. 

  CSRF protection has not     State-changing forms    Create a separate
  been reviewed               may need protection     follow-up issue
                              against cross-site      
                              request forgery. Decide 
                              the initial security    
                              scope with the team     
                              lead.                   

  Missing or invalid edit IDs A missing, malformed,   Handle during Java
  may not be handled          or unknown ID can lead  implementation
  consistently                to warnings, incorrect  
                              behavior, or unclear    
                              user feedback. Verify   
                              current handling.       

  No automated test suite is  Regressions may not be  Handle during Java
  identified in the stated    caught consistently as  implementation
  baseline                    the application is      
                              rebuilt. Confirm the    
                              repository's existing   
                              tests before concluding 
                              none exist.             

  `test.php` reportedly       The manual example may  Create a separate
  refers to `employee`, while be stale or             follow-up issue
  the README documents        inconsistent with the   
  `users`                     current schema. Verify  
                              its SQL and intended    
                              purpose.                

  SQLite differs from MySQL   SQL assumptions and     Verify during
  in physical types,          deployment paths may    acceptance testing
  generated-ID syntax, and    not transfer directly.  
  database-file deployment    The logical schema can  
  behavior                    remain the same while   
                              database-specific       
                              details change.         

  Existing MySQL records will Users or test records   Create a separate
  not be transferred          in MySQL will not       follow-up issue
                              appear in the new       
                              SQLite database. The    
                              team must confirm that  
                              this is acceptable for  
                              the course project.     

  Reverse-engineering         If the documents are    Create a separate
  overview/diagram documents  absent, the Java        follow-up issue
  may be missing              implementation cannot   
                              be compared against     
                              those artifacts until   
                              they are prepared or    
                              the team agrees on an   
                              alternative baseline.   
  ---------------------------------------------------------------------------

No mitigation is implemented by this document. These are planning
observations for later implementation, testing, or team decisions.

## 4.6 Explicit non-goals

This issue does not:

-   implement Java or Spring Boot;
-   create the SQLite database;
-   migrate data from MySQL;
-   change the PHP application;
-   choose a frontend framework;
-   add REST APIs;
-   add authentication unless the team creates a separate
    decision/issue;
-   delete or retire PHP files;
-   solve every security issue;
-   install dependencies or change configuration;
-   create the proposed forward-engineering issues.

## 4.7 Recommended forward-engineering issue breakdown

1.  **Initialize Maven and Spring Boot project structure.** This
    establishes the build and application skeleton that all subsequent
    Java implementation issues depend on.
2.  **Configure SQLite and create the users schema.** This depends on
    the project skeleton so the database configuration and schema can be
    integrated into the application.
3.  **Implement the Java User entity, repository, and service layer.**
    This depends on the project and schema decisions so the model and
    data-access behavior match the target database.
4.  **Implement the list-users page.** This depends on the entity,
    repository, and service so the controller can retrieve users and
    render them through Thymeleaf.
5.  **Implement the add-user page and validation.** This depends on the
    shared layout and data-access/service layers so submitted user data
    can be validated and saved.
6.  **Implement the edit-user page and validation.** This depends on the
    same model and service layers, plus the established form and
    validation approach, to load and update a selected user.
7.  **Implement the delete-user flow with safe request semantics.** This
    depends on the list page and service layer so the user can initiate
    a safe delete operation and return to the list.
8.  **Add automated tests.** This depends on the implemented CRUD and
    validation behavior so tests can verify the actual application paths
    and expected outcomes.
9.  **Compare Java behavior with the reverse-engineering documents.**
    This depends on the Java features and relevant reverse-engineering
    documents being available so differences can be identified against
    the baseline.
10. **Update README and retire PHP implementation only after
    acceptance.** This depends on successful tests, behavior comparison,
    and team acceptance so the Java instructions are ready before the
    old implementation is retired.

These are recommendations only. Do not create the issues as part of this
documentation task.

## 4.8 Open questions and decisions already made

### Decisions already made

-   Use Java with Spring Boot.
-   Use Spring MVC with server-rendered Thymeleaf views.
-   Use Maven.
-   Use SQLite.
-   Keep the same logical `users` schema.
-   Start with a fresh, empty SQLite database.
-   Do not migrate existing MySQL data.
-   Preserve user list, add, edit, and delete functionality.

### Open questions for the team lead

-   Should Bootstrap styling be preserved, replaced, or simplified?
-   What exact mobile-number validation rule should be used?
-   Should authentication be added in this course scope?
-   Should Spring Security/CSRF protection be included in the first Java
    iteration?
-   Should Java replace PHP in the same repository or a separate
    module/folder?
-   What Java version should be used?
-   How will the Java application be run and demonstrated?
-   Are `docs/reverse-engineering/current-system-overview.md` and
    `docs/reverse-engineering/current-system-diagrams.md` available, and
    if not, who will prepare them?

These questions remain open and must not be answered by guessing in this
issue.

------------------------------------------------------------------------
