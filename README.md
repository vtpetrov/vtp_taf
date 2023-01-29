Veselin Petrov's skeleton for a test automation framework which is able to:
- test APIs
- test UI (Selenium)
- has BDD capabilities (Cucumber) (TODO)

This is a **Maven** project which uses: 
- Java 19, 
- Junit 5, 
- Selenium 4 
- and so on.

See more in the pom.xml file.

## Command line options:

### include.tags:
1. **include.tags** - run tests with specific Junit tags
   <br/>Usage:
    - `-Dinclude.tags=ui`: run only tests tagged with `ui` tag
    - `-Dinclude.tags="ui|api"`: run tests tagged with `ui` **or** `api` tag (one of them is enough)
    - `-Dinclude.tags="endava&api"`: run tests tagged with `endava` **and** `api` tag (both have to be present on a test)
      
   (:information: **!!!** Use double quotes "" to surround the values if there are special chars or spaces )

### exclude.tags:
2. **exclude.tags** - Similar to the above, but used to **exclude** tests tagged with the particular tag!

### env:
3. **env** - Use this to load specific `*.properties` file
   <br/>Usage:<br/> 
   The value passed as `env` parameter need to be part of the file name holding the given properties.
   <br/> E.g. if we use `-Denv`=**dev**, and there is property file named "myprops-**dev**.properties",
   then this file will be loaded and the properties contained inside will be available

### disable.console.output
4. **disable.console.output** - to print or not verbose output on the console

   Usage:
   - enable:  `-Ddisable.console.output=false`
   - disable:  `-Ddisable.console.output=true`


#### Examples:
- `mvn clean test -Ddisable.console.output=true "-Dinclude.tags=endava&api"`
- `mvn clean test -Denv=dev -Ddisable.console.output=false -Dinclude.tags=ui`
- `mvn clean test -Denv=stage -Ddisable.console.output=false -Dinclude.tags=ui`

----

### ENDAVA:
See details related to Endava task in [endavaReadme.md](src%2Ftest%2Fjava%2Fendava%2FendavaReadme.md)



----

#### Disclaimer:
This project is distributed under the "GNU GENERAL PUBLIC LICENSE".
<br/> See more here [LICENSE](LICENSE)