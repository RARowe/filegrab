# Java Unshackled

## A summarized history of my programming career

I remember where I was the day I hit programming mastery. My friends had just left our university library following a study session, and I found myself with a little time on my hands. I cracked open the latter chapters of my Introduction to Java Programming book (I called it the Java hummingbird book), and tested my mettle against the chapters of GUI programming. This was unheard of. In my "Introduction to Programming" class, we had only covered the basics... conditionals, loops, functions... to jump to building something useful was a moonshot. I poured over the pages for hours trying and trying again, hitting the "play" button in my Eclipse editor, when finally, it happened. A window. A button. The button displayed text in my console when clicked. I had done it. The only logical step from there was building software for spacecraft.

<img src="java_book.jpg">

I obviously had not become a programming master that day, but, hagiography aside, I learned the fundamentals of programming with Java, and building GUIs with `javax.swing` made me fall in love with programming. It made me realize that I had chosen the correct path in college. My love for Java quickly fell away, but not because of outright distaste. I was getting introduced to new technologies every day, and I quickly forgot about my `javax.swing` programs. First it was C++ for a data structures class, then C for operating systems, then C# for an internship building dot.net applications, then Ruby ("Wow! I don't have to compile!"), then Haskell, then a short stint with Java again, before starting my career (C# and other languages).

Time passed. About a decade later, I returned to Java, but this time, in a professional setting. Java on the web. I traded my `swing` for `spring (boot)`. I began to see the memes I heard about Java manifesting in real life. Deep package names (`com.foo.bar.package.subpackage.utils.UtilsV2`), `AbstractFactoryProviderFactory`s, slow build times, and many more. What is "Maven"? We had Ant scripts, but I didn't understand those either. I can only use Intellij? Why are there so many annotations everywhere? My thought was "well, this must be Java now... I guess the state of the art has changed".

## A suitable alternative

I was relieved when I got moved to a team that used Golang. Well, "relief" may not be the right word. I had already heard all the reasons online as to why I was not "supposed" to like Go, and I bought into most of them without actually trying it (like any good online software zealot), but I thought I would give it a try for career development. I quickly learned to love it. Fast build times, static binaries, no heavy use of GOF patterns, mostly sane package management (I got in after the addition of `go.mod`)... these things pulled me in.
<img src="button_no_generics.jpg">(How could anyone possibly write a program without generics???)

I could sing the praises of Go all day, but that is not why I'm writing. At this point I use Go for just about everything. I was also going to use it for a simple program that was described to me, but my original solution wasn't as simple as I was hoping.

## The Problem

My wife introduced me to one of her childhood friends. We quickly got onto the topics of our professions, and he was excited to find out that I was a software engineer. He immediately dove into a story about a problem he recently solved for his small business with software. He runs a small analog to digital media company where he converts old VHS tapes, pictures and music to digital format. One of the problems he deals with on a regular basis is file management, specifically, manipulating file names. "I have to copy the full path of the file, and erase parts of it, and do [this], [that], and [the other]," he tells me. Most of the details he went into escape me now.

He later told me of the one problem he couldn't solve with software. A problem that took him hours every week. To cut his description of the problem short, he needed to be able to do the following:

1. List the contents of a directory
2. Get the path of each file/subdirectory
3. Remove the leading absolute path and get the basename of the path
   1. If possible, remove the file extension

He asked me, "is what I'm describing even possible?" I smiled, but didn't respond. If he was using a Unix-style machine, one could simply `ls | xargs basename` in the terminal or even do something slightly more intricate to remove the file extension. I imagine something similar could be done in Windows with Powershell. He used both Mac and Windows to do his job, so both would have worked well for him. My interlocutor, though tech-savvy, didn't seem like the type to want to learn how to use something like the command-line. He had a simple task that he wanted to solve, without the overhead of learning more things. I understood, and thought to myself "I'll crank out a solution for this problem and send him the executable before the day is over." I didn't tell him that, though. I wanted it to be a surprise.

NOTE: As an aside, my sister-in-law cleverly suggested to ask ChatGPT to do perform the operation he required, and it worked perfectly.

## The First Solution

"Easy," I thought to myself. I'll just write a program in Go that lists the files with no extension. I can even add simple functionality to additionally create a CSV with other information about the files (name, extension, size, etc). I laughed when I wrote the rudimentary solution. I showed my wife the code. "Look. This is the gist of what he needs." It was something like the following:

```
dirs, err := os.ReadDir(dirpath)
if err != nil {
    panic(err)
}

for _, d := range dirs {
    fmt.Println(fileNameWithoutExtension(d.Name()))
}
```

Simple! Now all I had to do was add some more features and spruce it up a bit. My thought for "sprucing it up" was to make a simple web application that he could run, access from localhost, select a folder from a file upload picker, and the app would list all the entries. Well... hold on. Some readers may know the problem with this approach already. The file upload file chooser in browsers doesn't allow you to get a full filepath; only the filename. Then a "backup plan" came to mind, "I know, I'll just make a web file browser. The root path will be the root of his filesystem, and he can navigate the app similar to indexed Nginx pages." That too seemed insufficient. What would it look like? Would it be a bunch of work (no, as it turns out, but that's not the point of this story :)<sup>1</sup>)? Maybe there was an even simpler solution.

There _was_ a simpler solution, but I didn't like it at first. Obviously the simplest solutions involve the command line, but as stated earlier, I didn't want to go near that for the sake of my new friend. However, what if the program was not a command-line tool _per se_, but used the terminal as its UI<sup>2</sup>? Like a "War Games" or "Zork" style program. That was it. Simple. Easy (enough) to use. Took me no time to make.

<img src="original_program">

The tool was done. It was clunky, but it was done. You had to do a lot of copy/paste work, but it was, you guessed it, done. The flow worked like this:
1. User starts program
2. A terminal window (should) pop up asking for a filepath (more on this later)
3. The user copies the filepath they want to the prompt
4. Another prompt is given for the output path for the results (one simple result file and one CSV file with more information)
5. The program exits
6. The user does something with the file outputs
I said it was clunky, but it did what my friend needed, and it beat his "by hand" editting of every filepath he needed to process. Then came the easy part... or so I thought; distribution! The Go build tools make it trivially simple to cross compile binaries from a single computer, which is what I needed, considering I was writing the software on a Linux laptop, and I needed to target Windows and Intel Macs. After building, I packaged them up in a zip with a readme email, tried to send them via Gmail and Google Drive, but failed (Google assumed it was malware), so I posted the zip for download on my private server. The only thing now, was to wait. It wasn't the best thing I ever made, but maybe I could get some good feedback in the morning.

I never got that feedback. All I got was a "it doesn't work on Windows, but I'll try it on my Mac later". Hmm... there could be any number of reasons why it didn't work, and I was too impatient to walk with him through what may have happened. Additionally, I did not possess a Windows computer of my own to do the testing. Using it on a Mac was a little better (I only had to right-click "Open"), but I was watching my already clunky solution fall into more and more friction. I wasn't proud of what I was delivering. Did it matter? I barely knew this guy and he wasn't paying, so probably not. Like most programmers, I couldn't stop thinking about the problem, and the inadequacy of my solution. Suddenly, I remembered.

## FearAndLoathingFactoryProviderStrategy.java
I remembered my love for Java, and it's supposed ability to "compile once, run anywhere," but I didn't even know where to begin. Embarrassingly, though I have worked with Java professionally (for more than a few years), I admit that I don't know much about it. I don't know how to build and run it from a terminal (the Eclipse "Play" button circa 2011 will always be burned into my memory). I don't understand all of the VM arguments. I don't know the correct libraries or build systems to use. "Do I still have to use a full blown IDE," I asked myself. Most importantly... did `javax.swing` still exist?

For those that are reading that are Java officianados, my anxieties towards using Java may be very sophmoric, but let the reader understand: what I knew about Java 13 years ago, versus what I knew Java to be today were totally different things. When I learned, everything was done on localhost. Java 6 was having a 7+ year run of being the standard Java version. Not every large tech company produced their own version of the OpenJDK. Granted, I imagine the simplicity of what I was doing also had to do with being in an educational environment, but my point still stands. When working with Java now, everything felt so much more complicated. Everything. Try debugging a large Java Springboot application. It's about the same as debugging a configuration file, because there is no "real code" to look at. Have you ever gotten to the bottom of everything that build tools like Maven and Gradle do? I haven't. Is you classpath not right? Too bad... I can't help you. I don't even fully understand what a classpath is. Then, there's the application structure... OH, the application structure. Get ready to open a medium sized project that has more files than the Linux Kernel source tree. Good thing I can explore the code by "F12'ing around" ("F12" used to be a common key for "jump to definition"). Wait... this is just an interface. Better look at all the implementations. Oh. There is only one. Glad they seperated the concerns (/s). What's this? Looks like somebody still loves inheritance in 2024. Better work my way up the inheritance tree to see what's actually going on. Ah... there they are! The GOF patterns! Patterns everywhere! Simple code made complicated, but hey, Martin Fowler will praise you for keeping it "CLEAN". Also, for some reason, all of the code lives deep inside the `src` folder. To get to your `Main.java` you need to go through, `src/java/com/company/project/program/Main.java`. Fortunately, IntelliJ will make these deep filepaths act like a single file. You don't use IntelliJ? "You're going to need to leave your current editor, because we basically can't run this thing without it being propped up by the world's most complicated IDE... We had to install IntelliJ on the server to make sure our app could run properly."

<img src="crazy java.jpg">(See! It's this simple!)

I was being cheeky, but there is a reason why Java has this stereotype. So many Java projects look and feel the same, and they carry with them years and years of changing practice. From the outside, the ecosystem and tooling seems fragmented, and when I try to get anything done, the only thing that pops up are Baeldung articles.

To those who are not happy with my treatment of Java, I think that's reasonable. I'm not being fair. All the things I scoffed at... these things too can be learned, and honestly may not be that bad. Quite frankly, most of my problems with Java are probably a skill issue. That being said this is what was going through my head when I decided to possibly try rewriting my solution in Java. That is, until I actually tried.

## Simplicity under all those Beans
My thought was this, "I'm going to rebuild the Filegrab in Java," this is what I had started calling my modest program, "but I'm going to do it my way." First things first, no build tools. I don't need them. Surely you can compile Java in the terminal without too much issue. Secondly, only one source file (and, by extension, one class). This will make absence of build tools easier. Having everyting in one file does not match usual Java style, but I don't care. I don't care that things are supposed to be "objects communicating by passing messages." Despite being marketed as object oriented, most Java code ends up being written in an imperative style. Even more funny is when objects get created to have a single method called on them, and then are garbage collected.<sup>4</sup> Finally, it needed to be a `javax.swing` app. A GUI would make things easier for my friend, but, more importantly, I needed to be reunited to the library that made me fall in love with programming.

Okay. The timer has started.

Let's get a "Hello world" program going. First things first, I actually need Java on my laptop. As always, I turned to [asdf](https://asdf-vm.com/) and its [Java Plugin](https://github.com/halcyon/asdf-java) to manage my install. With Java sitting sweetly on my filesystem, I opened my editor, created a new file, wrote out my class boilerplate, didn't bother with the `package` directive so I wouldn't need subfolders to store my code, and typed the magical `public static void main(String[] args)` incantation that has lived in my head since freshman year. Okay. That looks good. The language server isn't throwing any errors. Let's compile and run this. After a quick search online, I found that compilation was a snap. All I needed was `javac Main.java && java Main`. Bingo. I love it. No `mvn clean install -Dmaven.test.skip=true exec:java -Dexec.mainClass="Main"`.

That's when it happened. I entered an ethereal realm between heaven and earth. My next hour consisted of me copy/pasting old Oracle `javax.swing` code samples, and tweaking the outputs. More and more components I poured on. Dare I add a `JMenuBar`? Of course. This romance needs to be fully rekindled. Everything felt so fast.

When I"m not using "standard practice" my experience of Java was really great. That must be saying something.
<sup>4</sup>I can't find the video, but there used to be a funny talk only about refactoring Python, and the refrain was "if your class has two methods, and one is the constructor, turn it into a function".
