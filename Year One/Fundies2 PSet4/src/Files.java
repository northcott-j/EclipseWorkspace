//Assignment 4
//Northcott Jonathan
//nrthcoj
//Lowen Zach
//zlowen

import tester.Tester;

//to represent different files in a computer
interface IFile {

    // compute the size of this file
    int size();

    // compute the time (in seconds) to download this file
    // at the given download rate
    int downloadTime(int rate);

    // is the owner of this file the same 
    // as the owner of the given file?
    boolean sameOwner(AFile that);
}

//The abstract class AFile
//Every file as a name and owner
abstract class AFile implements IFile {
    String name;
    String owner;

    AFile(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }
    
    /* TEMPLATE:
     * Fields:
     * this.name --- String
     * this.owner --- String
     *
     * Methods
     * this.size() --- int
     * this.downloadTime(int) --- int
     * this.sameOwner(AFile) --- boolean
     *
     */

    // compute the size of this file
    // is different for each type
    abstract public int size();

    // compute the time (in seconds) to download this file
    // at the given download rate
    public int downloadTime(int rate) {
        if (this.size() % rate == 0) {
            return this.size() / rate;
        }
        else {
            return this.size() / rate + 1;
        }
    }

    // is the owner of this file the same 
    // as the owner of the given file?
    public boolean sameOwner(AFile that) {
        return this.owner.equals(that.owner);
    }
}

//to represent a text file
class TextFile extends AFile {
    // Length of the textfile
    int length;   // in bytes

    TextFile(String name, String owner, int length) {
        super(name, owner);
        this.length = length;
    }

    /* TEMPLATE:
     * Fields:
     * this.name --- String
     * this.owner --- String
     * this.length --- int
     *
     * Methods
     * this.size() --- int
     * this.downloadTime(int) --- int
     * this.sameOwner(AFile) --- boolean
     *
     */

    // compute the size of this file
    public int size() {
        return this.length;
    }  
}

//to represent an image file
class ImageFile extends AFile { 
    // Width of the image
    int width;   // in pixels

    // Height of the image
    int height;  // in pixels

    ImageFile(String name, String owner, int width, int height) {
        super(name, owner);
        this.width = width;
        this.height = height;
    }

    /* TEMPLATE:
     * Fields
     * this.name --- String
     * this.owner --- String
     * this.width --- int
     * this.height --- int
     *
     * Methods
     * this.size() --- int
     * this.downloadTime(int) --- int
     * this.sameOwner(AFile) --- boolean
     *
     */

    // compute the size of this file
    public int size() {
        return this.width * this.height;
    }  
}


//to represent an audio file
class AudioFile extends AFile {

    // Quality of the file e.g. 320kbps
    int speed;   // in bytes per second

    // Time of the audiofile
    int length;  // in seconds of recording time

    AudioFile(String name, String owner, int speed, int length) {
        super(name, owner);
        this.speed = speed;
        this.length = length;
    }

    /* TEMPLATE:
     * Fields
     * this.name --- String
     * this.owner --- String
     * this.speed --- int
     * this.length --- int
     *
     * Methods
     * this.size() --- int
     * this.downloadTime(int) --- int
     * this.sameOwner(AFile) --- boolean
     *
     */

    // compute the size of this file
    public int size() {
        return this.speed * this.length;
    }  
}

//This is the class that contains the data examples and method tests
class ExamplesFiles {

    AFile text1 = new TextFile("English paper", "Maria", 1234);
    AFile picture1 = new ImageFile("Beach", "Maria", 400, 200);
    AFile song1 = new AudioFile("Help", "Pat", 200, 120);
    AFile text2 = new TextFile("Fundies paper", "Marsha", 666);
    AFile picture2 = new ImageFile("Lab", "Kyle", 600, 600);
    AFile song2 = new AudioFile("Dance", "Jerry", 10, 120);

    // test the method size for the classes that represent files
    boolean testSize(Tester t) {
        return
                t.checkExpect(this.text2.size(), 666) &&
                t.checkExpect(this.picture2.size(), 360000) &&
                t.checkExpect(this.song2.size(), 1200) &&
                t.checkExpect(this.text1.size(), 1234) &&
                t.checkExpect(this.picture1.size(), 80000) &&
                t.checkExpect(this.song1.size(), 24000);
    }

    // test the method downloadTime for the classes that represent files
    boolean testDownloadTime(Tester t) {
        return
                t.checkExpect(this.text2.downloadTime(10), 67) &&
                t.checkExpect(this.picture2.downloadTime(10), 36000) &&
                t.checkExpect(this.song2.downloadTime(20), 60) &&
                t.checkExpect(this.text1.downloadTime(10), 124) &&
                t.checkExpect(this.picture1.downloadTime(20), 4000) &&
                t.checkExpect(this.song1.downloadTime(10), 2400);
    }

    // test the method sameOwner for the classes that represent files
    boolean testSameOwner(Tester t) {
        return
                t.checkExpect(this.text2.sameOwner(text1), false) &&
                t.checkExpect(this.picture2.sameOwner(picture2), true) &&
                t.checkExpect(this.song2.sameOwner(picture1), false) &&
                t.checkExpect(this.text1.sameOwner(picture1), true) &&
                t.checkExpect(this.picture1.sameOwner(text1), true) &&
                t.checkExpect(this.song1.sameOwner(song2), false);
    }
}