# antlr4-file-engine
Automatically generates Java and JavaScript files based on information retrieved from a plain text file.

This is a simple Java application that uses ANTL4 to read a raw text file and generate Java and Javascript files that basically contain the same data.  

ANTL4 is included as a maven plugin
````xml
<artifactId>maven-compiler-plugin</artifactId>
````
## When can this be useful?
The classic example is to make sure your Java POJOs map correcly to JSon objects.   

Another example is that when the frontend sends an http request, the server responds with data that both the backend and the frontend unambiguously interpret in the same way.

## Before you start
Run maven clean and install.  
````bash
mvn clean install
````
This will let ANTL4 generate Java sources used to perform the lexing and parsing phrases on your text file, i.e.
if your grammar is named `Mapping.g4` then a `MappingLexer.java`, `MappingParser.java` and `MappingBaseListener.java` are created.  
Analogously, if your grammar is `math.g4`, then a `mathLexer.java`, `mathParser.java` and `mathBaseListener.java` are created. The later goes
against the Java convention of capitalizing class names, so make sure your grammar is also capitalized.


## How it works?
- Edit `resources/datasource.txt` with the information you wish to place in your Java and JavaScript files.  
This information has to respect the rules defined in the `antlr4/Mapping.g4` grammar;
- Run `Generate.main()` to generate the Java and JavaScript files into the `output` directory.

## Examples
Here is a very simple example, where the `resourses/datasource.txt` contents is
````txt
SampleFile
================
foo = 3
````

The first line sets the name for the Java and Javascript files. The second line is simply a separator and finally the data section that contains only one assignment.  

When you run `Generate.main()` the output is
````txt
Generating Java and Javascript files from resources/datasource.txt ...
Created /Users/ruisoftware/work/antlr4-file-generator/output/SampleFile.java
Created /Users/ruisoftware/work/antlr4-file-generator/output/sampleFile.js

Process finished with exit code 0
````

The generated Java file is placed at `output/SampleFile.java`
````java
public class SampleFile {

    private static final int foo = 3;
    
}
````
and the correspondent JavaScript file at `output/sampleFile.js`
````javascript
var foo = 3;
````

Now, let's add a more complex `bar`
````txt
SampleFile
================
foo = 3

bar = map (
        a = -1.2
        b = 0
        c = [2, hello, [3]]
        d = map (
              d1 = 2
              d2 = 200
           )
        e = []
    )
````
You can use integers, floating-point numbers, strings, maps and arrays in any combination. Arrays and maps can include other data of any type at any deepness level.  
This time, the generated `output/SampleFile.java` is

````java
import java.lang.Integer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SampleFile {

    private static final int foo = 3;
    
    private static final Map<String, Object> bar = new LinkedHashMap<String, Object>() {{
        put("a", new Float(-1.2f));
        put("b", new Integer(0));
        put("c", new LinkedList<Object>() {{
            add(new Integer(2));
            add("hello");
            add(new LinkedList<Integer>() {{
                add(new Integer(3));
            }});
        }});
        put("d", new LinkedHashMap<String, Integer>() {{
            put("d1", new Integer(2));
            put("d2", new Integer(200));
        }});
        put("e", new LinkedList<Object>());
    }};
    
}
````
and the `output/sampleFile.js`
````javascript
var foo = 3;

var bar = {
    "a": -1.2,
    "b": 0,
    "c": [
        2,
        "hello", [3]
    ],
    "d": {
        "d1": 2,
        "d2": 200
    },
    "e": []
};
````

# License
This project is licensed under the terms of the [MIT license](https://opensource.org/licenses/mit-license.php)

# Bug Reports & Feature Requests
Please use the [issue tracker](https://github.com/ruisoftware/antlr4-file-generator/issues) to report any bugs or file feature requests.
