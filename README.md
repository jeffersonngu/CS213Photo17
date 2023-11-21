# README

## To run

From the root of the repository, run the following:

```bash
$ javac -d bin -cp src/main/java -sourcepath src/main/java src/main/java/module-info.java $(find src/main/java -name "*.java")  -cp src/main/resources
$ java -cp bin:src/main/resources/ com.photos.Photos
```
You may notice there is a `Launcher` class with its own main, this is a wrapper so that the project can be compiled into a jar by Maven. Do not run this otherwise and use `Photos.main` instead

To generate javadocs in docs, run `mvn javadoc:javadoc`. The docs are located at `docs/index.html`, which can be opened in a browser.

To build the jar, run `mvn package`

## Assumptions/Notes for Grader
1. Copy and Move photo to another album disallows selection of albums that already contain the photo.
2. While it asks "A user can define their own tag type and add it to the list (so from that point on, that tag type will show up in the preset list of types the user can choose from)", it does not say we have to implement a way to remove these custom tags or a way to change their single/multi value nature. In other words, assume they can be permanent.
3. Javadocs does not require @version, @author, @since, or @use. Though annotations such as @param and @return are necessary
4. Folder data has `.gitkeep` simply to keep git happy, it is not relevant (even so data folder will be generated if not present)
5. The `data/stock` folder will (re)generate after all Users are deleted or there does not exist a username.dat in data folder (it will also (re)generate the images if they are deleted).
6. Grader does not need to build a jar or use `Launcher`, those exist for fun

## Project Members
Jefferson Nguyen, Arnav Borborah