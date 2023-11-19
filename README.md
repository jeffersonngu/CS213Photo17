# README

This README would normally document whatever steps are necessary to get your
application up and running.

## To run

From the root of the repository, run the following:

```bash
$ javac -d bin -cp src/main/java -sourcepath src/main/java src/main/java/module-info.java $(find src/main/java -name "*.java")  -cp src/main/resources
```

## Assumptions
1. Copy and Move photo to another album disallows selection of albums that already contain the photo.
2. While it asks "A user can define their own tag type and add it to the list (so from that point on, that tag type will show up in the preset list of types the user can choose from)", it does not say we have to implement a way to remove these custom tags or a way to change their single/multi value nature. In other words, assume they can be permanent.