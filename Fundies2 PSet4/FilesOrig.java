// CS 2510 Fall 2014
// Assignment 4

import tester.Tester;

// to represent different files in a computer
interface IFile {
    
    // compute the size of this file
    int size();
    
    // compute the time (in seconds) to download this file
    // at the given download rate
    int downloadTime(int rate);
    
    // is the owner of this file the same 
    // as the owner of the given file?
    boolean sameOwner(IFile that);
}

// to represent a text file
class TextFile implements IFile {
    String name;
    String owner;
    int length;   // in bytes
    
    TextFile(String name, String owner, int length) {
        this.name = name;
        this.owner = owner;
        this.length = length;
    }
    
    // compute the size of this file
    public int size() {
        return this.length;
    }  
    
    // compute the time (in seconds) to download this file
    // at the given download rate
    public int downloadTime(int rate) {
        return this.size() / rate;
    }
    
    // is the owner of this file the same 
    // as the owner of the given file?
    public boolean sameOwner(IFile that) {
        return this.owner.equals(that.owner);
    }
}

//to represent an image file
class ImageFile implements IFile { 
    String name;
    String owner;
    int width;   // in pixels
    int height;  // in pixels
    
    ImageFile(String name, String owner, int width, int height) {
        this.name = name;
        this.owner = owner;
        this.width = width;
        this.height = height;
    }
    
    // compute the size of this file
    public int size() {
        return this.width * this.height;
    }  
    
    // compute the time (in seconds) to download this file
    // at the given download rate
    public int downloadTime(int rate) {
        return this.size() / rate;
    }
    
    // is the owner of this file the same 
    // as the owner of the given file?
    public boolean sameOwner(IFile that) {
        return true;
    }
}


//to represent an audio file
class AudioFile implements IFile {
    String name;
    String owner;
    int speed;   // in bytes per second
    int length;  // in seconds of recording time
    
    AudioFile(String name, String owner, int speed, int length) {
        this.name = name;
        this.owner = owner;
        this.speed = speed;
        this.length = length;
    }
    
    // compute the size of this file
    public int size() {
        return this.speed * this.length;
    }  
    
    // compute the time (in seconds) to download this file
    // at the given download rate
    public int downloadTime(int rate) {
        return this.size() / rate;
    }
    
    // is the owner of this file the same 
    // as the owner of the given file?
    public boolean sameOwner(IFile that) {
        return true;
    }
}

class ExamplesFiles {
    
    IFile text1 = new TextFile("English paper", "Maria", 1234);
    IFile picture1 = new ImageFile("Beach", "Maria", 400, 200);
    IFile song1 = new AudioFile("Help", "Pat", 200, 120);
    IFile text2 = new TextFile("Fundies paper", "Marsha", 666);
    IFile picture2 = new ImageFile("Lab", "Kyle", 600, 600);
    IFile song2 = new AudioFile("Dance", "Jerry", 10, 120);
    
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
    
    boolean testDownloadTime(Tester t) {
        return
            t.checkExpect(this.text2.downloadTime(10), 66) &&
            t.checkExpect(this.picture2.downloadTime(10), 36000) &&
            t.checkExpect(this.song2.downloadTime(20), 60) &&
            t.checkExpect(this.text1.downloadTime(10), 123) &&
            t.checkExpect(this.picture1.downloadTime(20), 4000) &&
            t.checkExpect(this.song1.downloadTime(10), 2400);
    }
}