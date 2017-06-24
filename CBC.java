import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.SwingUtilities;
import java.text.*;
import javax.swing.JOptionPane;
import java.lang.reflect.InvocationTargetException;
import static java.lang.Math.sqrt;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.*;
import java.util.*;
import java.util.StringJoiner;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import sun.audio.*;
import java.util.Random;
public class CBC extends JPanel implements PropertyChangeListener {
    public Dict d = new Dict();
    public String[][] words = d.define();;
    public String compName = "CBC";
    public String user = "";
    public String videoDir = "/Users/axzxc/Videos";
    public String musicDir = "/Users/axzxc/Music";
    public String defaultBrowser = "firefox#s#";
    public String defaultEditor = "gedit#s#";
    public String defaultDir = "/";
    public String defaultPlayer = "C:/Program Files (x86)/VideoLAN/VLC/vlc";
    public String defaultSearch = "https://www.google.com/search?q=";
    public String joined;
    public static ImageIcon icon = new ImageIcon("brain.png");
    public String config = "config.cfg";
    ArrayList<String> content = new ArrayList<String>();
    private double pi = Math.PI;
    private String text;
    private String translation;
    private JButton helpButton = new JButton("Help");
    public String[] searchEngines = {
        "https://www.google.com/search?q=",
        "https://www.bing.com/search?q=",
        "https://www.youtube.com/results?search_query=",
        "https://www.google.com/search?tbm=isch&q=",
        "https://duckduckgo.com/?q=",
        "http://en.wikipedia.org/w/index.php?search=",
        "http://www.urbandictionary.com/define.php?term="
    };
    //Labels to identify the fields
    private JLabel textLabel;
    private JLabel translationLabel;
    
    private JFormattedTextField textField;
    private JFormattedTextField translationField;
    
    private NumberFormat textFormat;
    private NumberFormat translationFormat;
    
    public CBC() {  
        super(new BorderLayout());
        //Beginning methods
        reload();
        //helpButton stuff
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                translationField.setValue("Opening README.TXT");
                String line = null;
                String readmeContents = "";
                int lineInt = 0;
                try {
                    FileReader filereader = new FileReader(defaultDir+"README.TXT");
                    StringJoiner joiner = new StringJoiner("");
                    BufferedReader bufferedreader = new BufferedReader(filereader);
                    while((line = bufferedreader.readLine()) != null) {
                        joiner.add(line+"\n");
                    }
                    readmeContents = joiner.toString();
                    bufferedreader.close();
                } catch(FileNotFoundException ex) {
                    System.out.println("'"+defaultDir+"README.TXT+"+"' not found!");
                } catch (IOException ex) {
                    System.out.println("ERROR");
                }
                JFrame frame2 = new JFrame("Help");
                frame2.setIconImage(icon.getImage());
                JTextArea textArea = new JTextArea(5, 25);
                textArea.append(readmeContents);
                textArea.setEditable(false);
                //Add contents to the window.
                frame2.add(textArea);
                //Display the window.
                frame2.pack();
                frame2.setVisible(true);
            }
        });
        //Create the labels.
        textLabel = new JLabel("Enter command: ");
        translationLabel = new JLabel("Output: ");
        
        //Create the text fields and set them up.
        textField = new JFormattedTextField(textFormat);
        textField.setValue(new String(""));
        textField.setColumns(25);
        textField.addPropertyChangeListener("value", this);
           
        translationField = new JFormattedTextField(textFormat);
        translationField.setValue(new String(""));
        translationField.setColumns(25);
        translationField.setEditable(false);
        translationField.setForeground(Color.blue);
        
        
        //Tell accessibility tools about label/textfield pairs.
        textLabel.setLabelFor(textField);
        translationLabel.setLabelFor(translationField);
        
        
        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel(new GridLayout(0,1));
        labelPane.add(textLabel);
        labelPane.add(translationLabel);
    
        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(new GridLayout(0,1));
        fieldPane.add(textField);
        fieldPane.add(translationField);
        
        JPanel buttonPane = new JPanel(new GridLayout(0,1));
        buttonPane.add(helpButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //Put the panels in this panel, labels on left,
        //text fields on right.
        //musicStart("/home/axzxc/Downloads/music/bleach.mp3");
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(labelPane, BorderLayout.WEST);
        add(buttonPane, BorderLayout.LINE_END);
        add(fieldPane, BorderLayout.CENTER);
    }
    /** Called when a field's "value" property changes. */
    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        if (source == textField) {
            text = (String)textField.getValue();
        }
        if (text.equals("")) {}
        else if (text.equals(" ")) {}
        else {
            translation = translate(text);
        }
        System.out.println(translation);
        translationField.setValue(new String(translation));
        textField.requestFocus();
    }
    public static JFrame thisFrame = new JFrame("CBC 1.4");
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's CBC.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowCBC(thisFrame);
            }
        });
    }
    public static void createAndShowCBC(JFrame j) {
        //Create and set up the window.
        JFrame frame = j;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(icon.getImage());
        //Add contents to the window.
        frame.add(new CBC());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    String translate(String t) {
        String[] sentence = t.split("\\s+");
        int i;
        int h;
        boolean output;
        int[] posArr;
        int pos = -2;
        int wpos = -2;
        posArr = match(sentence,words);
        pos = posArr[0];
        i = posArr[1];
        //words
        if (sentence.length == 1&&pos!=-1) {
            if (words[i][0].equals("close")) {
                System.exit(0);
            } else if (words[i][0].equals("reload")) {
                reload();
                return "Done!";
            } else if (words[i][0].equals("hello")) {
                return "Hello "+user+". I am "+compName+", your personal assistant.";
            } else if (words[i][0].equals("help")) {
                return "Click the help button.";
            }
            output = execute(defaultBrowser+defaultSearch+t);
            if (output) {
                return "Searching "+t+"...";
            } else {
                 return "ERROR";
            }
        }
        if (pos==-1){
            /**
            String math = stringMath(t);
            if (math.equals("")) {} else {
                return math;
            }            
            **/
            output = execute(defaultBrowser+defaultSearch+t);
            if (output) {
                return "Showing search results for "+t+"...";
            } else {
                return "ERROR";
            }
        } else if (words[i][0].equals("close")) { //close verbs
            System.exit(0);
        } else if (words[i][0].equals("reload")) { //reload verbs
            reload();
            return "Done!";
        } else if (words[i][0].equals("hello")) { //close verbs
            return "Hello "+user+". I am "+compName+", your personal assistant.";
        } else if (words[i][0].equals("execute")) { //execute verbs
            if (sentence[pos+1].contains(".com")||sentence[pos+1].contains(".ly")||sentence[pos+1].contains(".org")||sentence[pos+1].contains(".net")||sentence[pos+1].contains(".io")||sentence[pos+1].contains(".gov")){
                output = execute(defaultBrowser+sentence[pos+1]);
                if (output) {
                    return "Opening "+sentence[pos+1]+"...";
                } else {
                    return "ERROR";
                }
            } else if (sentence[pos+1].equals("config")||sentence[pos+1].equals("defaults")) {
                output = execute(defaultEditor+"config.cfg");
                if (output) {
                    return "Opening config...";
                } else {
                    return "ERROR";
                }
            }
            String cutted = cut(sentence,(pos+1));
            cutted = cutted.replace(" ","#s#");
            output = execute(cutted);
            if (output) {
                return "Starting "+sentence[pos+1]+"...";
            } else {
                return "ERROR";
            }
        } else if (words[i][0].equals("play")) { //play verbs
            if (t.toLowerCase().contains("youtube")) {
                StringJoiner joiner = new StringJoiner(" ");
                if (sentence[pos+1].equals("me")) {
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                } else {
                    for (h=0;h<(sentence.length-pos-1);h++) {
                        joiner.add(sentence[h+pos+1]);
                    }
                }
                joined = joiner.toString();
                output = execute(defaultBrowser+"https://www.youtube.com/results?search_query="+joined);
                if (output==true) {
                  return "Showing results for "+joined+"...";
                }
            } else {
                final File musicFolder = new File(musicDir);
                final File videoFolder = new File(videoDir);
                String rest = cut(sentence,(pos+1));
                String media;
                //music   
                listFilesForFolder(content,musicFolder);
                if (sentence[pos+1].equals("me")) {
                    if (sentence[pos+2].equals("something")) {
                        Random rand = new Random(); 
                        int value = rand.nextInt((content.size()-1));
                        output = execute(defaultPlayer+musicDir+content.get(value));
                        if (output) {
                            return "Playing "+content.get(value)+"...";
                        }
                    }
                } else {
                    if (sentence[pos+1].equals("something")) {
                        Random rand = new Random(); 
                        int value = rand.nextInt((content.size()-1));
                        output = execute(defaultPlayer+musicDir+content.get(value));
                        if (output) {
                            return "Playing "+content.get(value)+"...";
                        }
                    }
                }                
                media = mostMatched(rest,content);
                if (media.equals("")) {} else {
                    output = execute(defaultPlayer+musicDir+media);
                    if (output) {
                        return "Playing "+media+"...";
                    }
                }
                //video
                listFilesForFolder(content,videoFolder);
                media = mostMatched(rest,content);
                if (media.equals("")) {} else {
                    output = execute(defaultPlayer+videoDir+media);
                    if (output) {
                        return "Playing "+media+"...";
                    }
                }
                StringJoiner joiner = new StringJoiner(" ");
                if (sentence[pos+1].equals("me")) {
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                } else {
                    for (h=0;h<(sentence.length-pos-1);h++) {
                        joiner.add(sentence[h+pos+1]);
                    }
                }
                joined = joiner.toString();
                output = execute(defaultBrowser+"https://www.youtube.com/results?search_query="+joined);
                if (output==true) {
                  return "Showing results for "+joined+"...";
                }
            }
        } else if (words[i][0].equals("change")) { //change verbs
            if (Arrays.asList(sentence).contains("browser")) {
                if (sentence[pos+2].equals("to")) {
                    changeDefaults("browser",sentence[pos+3]);
                } else {
                    changeDefaults("browser",sentence[pos+2]);
                }
                return "Browser changed.";
            } else if (Arrays.asList(sentence).contains("name")||Arrays.asList(sentence).contains("user")||Arrays.asList(sentence).contains("username")) {
                if (sentence[pos+1].equals("my")) {
                    if (sentence[pos+3].equals("to")) {
                        changeDefaults("user",sentence[pos+4]);
                    } else {
                        changeDefaults("user",sentence[pos+3]);
                    }
                    return "Alright I'll call you "+user;
                } else if (sentence[pos+1].equals("your")) {
                    if (sentence[pos+3].equals("to")) {
                        changeDefaults("comp",sentence[pos+4]);
                    } else {
                        changeDefaults("comp",sentence[pos+3]);
                    }
                    return "Alright my name is "+compName;
                } else {
                    if (sentence[pos+2].equals("to")) {
                        changeDefaults("name",sentence[pos+3]);
                    } else {
                        changeDefaults("name",sentence[pos+2]);
                    }
                    return "Alright I'll call you "+user;
                }
            } else if (Arrays.asList(sentence).contains("editor")||Arrays.asList(sentence).contains("texteditor")) {
                if (sentence[pos+2].equals("to")) {
                    changeDefaults("editor",sentence[pos+3]);
                } else {
                    changeDefaults("editor",sentence[pos+2]);
                }
                return "Editor changed...";
            } else if (Arrays.asList(sentence).contains("defaults")||Arrays.asList(sentence).contains("config")||Arrays.asList(sentence).contains("configuration")) {
                output = execute(defaultEditor+"config.cfg");
                if (output) {
                    return "Opening...";
                } else {
                    return "ERROR";
                }
            } else if (Arrays.asList(sentence).contains("directory")||Arrays.asList(sentence).contains("home")) {
                if (sentence[pos+2].equals("to")) {
                    changeDefaults("directory",sentence[pos+3]);
                } else {
                    changeDefaults("directory",sentence[pos+2]);
                }
                return "Directory changed...";
            } else if (Arrays.asList(sentence).contains("search")||Arrays.asList(sentence).contains("engine")) {
                if (sentence[pos+2].equals("to")) {
                    changeDefaults("search",sentence[pos+3]);
                } else {
                    changeDefaults("search",sentence[pos+2]);
                }
                return "Directory changed...";
            } else if (Arrays.asList(sentence).contains("player")) {
                if (sentence[pos+2].equals("to")) {
                    changeDefaults("player",sentence[pos+3]);
                } else {
                    changeDefaults("player",sentence[pos+2]);
                }
                return "Player changed...";
            } else {
                StringJoiner joiner = new StringJoiner(" ");
                for (h=0;h<(sentence.length-pos-1);h++) {
                    joiner.add(sentence[h+pos+1]);
                }
                joined = joiner.toString();
                output = execute(defaultEditor+joined);
                if (output) {
                    return "Opening "+joined+"...";
                } else {
                    return "ERROR";
                }
            }
        } else if (words[i][0].equals("search")) { //search verbs
            String whichEngine = arrMatch(sentence,searchEngines);
             
            String str = cut(sentence,(pos+1));
            output = execute(defaultBrowser+defaultSearch+str);
            if (output) {
                return "Searching "+str+"...";
            } else {
                 return "ERROR";
            }
        } else if (words[i][0].equals("log")) {
            System.out.println(isStringNumber(sentence[pos+1]));
            return t;
        } else if (words[i][0].equals("who")) { //who questions
            if (sentence[pos+1].equals("are")&&sentence[pos+2].equals("you")) {
                return "I am "+compName+", your personal assistant.";
            } else if (sentence[pos+1].equals("you")) {
                return "I am "+compName+", your personal assistant.";
            } else if (sentence[pos+1].equals("am")) {
                if (user.equals("")) {
                  return "I don't know your name yet.";
                } else {
                  return "You're "+user+", of course.";
                }
            } else if (sentence[pos+1].equals("is")||sentence[pos+1].equals("was")||sentence[pos+1].equals("are")) {
                if (sentence[pos+2].toLowerCase().equals("a")||sentence[pos+2].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-3);h++) {
                        joiner.add(sentence[h+pos+3]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            }
            if (sentence[pos+1].toLowerCase().equals("a")||sentence[pos+1].toLowerCase().equals("the")){
                StringJoiner joiner = new StringJoiner(" ");
                for (h=0;h<(sentence.length-pos-2);h++) {
                    joiner.add(sentence[h+pos+2]);
                }
                joined = joiner.toString();
                output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                if (output) {
                    return "Searching...";
                } else {
                    return "ERROR";
                }
            } else {
                StringJoiner joiner = new StringJoiner(" ");
                for (h=0;h<(sentence.length-pos-1);h++) {
                    joiner.add(sentence[h+pos+1]);
                }
                joined = joiner.toString();
                output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                if (output) {
                    return "Searching...";
                } else {
                    return "ERROR";
                }
            }
        } else if (words[i][0].equals("what")) { //what questions
            if (sentence[pos+1].equals("does")) {
                output = execute(defaultBrowser+defaultSearch+t);
                if (output) {
                    return "Searching...";
                } else {
                    return "ERROR";
                }
            }
            if (sentence[pos+1].equals("is")||sentence[pos+1].equals("was")||sentence[pos+1].equals("are")) {
                if (sentence[pos+2].toLowerCase().equals("a")||sentence[pos+2].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-3);h++) {
                        joiner.add(sentence[h+pos+3]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else if (sentence[pos+2].equals("my")) {
                    if (sentence[pos+3].equals("name")) {
                        return "Your name is "+user+".";
                    }
                } else if (sentence[pos+2].equals("your")) {
                    if (sentence[pos+3].equals("name")) {
                        return "My name is "+compName+".";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            } else {
                if (sentence[pos+1].toLowerCase().equals("a")||sentence[pos+1].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else if (sentence[pos+1].equals("my")) {
                    if (sentence[pos+2].equals("name")) {
                        return "Your name is "+user+".";
                    }
                } else if (sentence[pos+1].equals("your")) {
                    if (sentence[pos+2].equals("name")) {
                        return "My name is "+compName+".";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-1);h++) {
                        joiner.add(sentence[h+pos+1]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            }
        } else if (words[i][0].equals("when")) { //when questions
            if (sentence[pos+1].equals("is")||sentence[pos+1].equals("was")) {
                if (sentence[pos+2].toLowerCase().equals("a")||sentence[pos+2].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-3);h++) {
                        joiner.add(sentence[h+pos+3]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            } else {
                if (sentence[pos+1].toLowerCase().equals("a")||sentence[pos+1].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-1);h++) {
                        joiner.add(sentence[h+pos+1]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+"http://en.wikipedia.org/w/index.php?search="+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            }
        } else if (words[i][0].equals("where")) { //where questions
            if (sentence[pos+1].equals("is")||sentence[pos+1].equals("was")) {
                if (sentence[pos+2].toLowerCase().equals("a")||sentence[pos+2].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-3);h++) {
                        joiner.add(sentence[h+pos+3]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+defaultSearch+"where%20is%20"+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+defaultSearch+"where%20is%20"+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            } else {
                if (sentence[pos+1].toLowerCase().equals("a")||sentence[pos+1].toLowerCase().equals("the")){
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-2);h++) {
                        joiner.add(sentence[h+pos+2]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+defaultSearch+"where%20is%20"+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                } else {
                    StringJoiner joiner = new StringJoiner(" ");
                    for (h=0;h<(sentence.length-pos-1);h++) {
                        joiner.add(sentence[h+pos+1]);
                    }
                    joined = joiner.toString();
                    output = execute(defaultBrowser+defaultSearch+"where%20is%20"+joined);
                    if (output) {
                        return "Searching...";
                    } else {
                        return "ERROR";
                    }
                }
            }
        } else if (words[i][0].equals("why")) { //why questions
            StringJoiner joiner = new StringJoiner(" ");
            for (h=0;h<(sentence.length-pos);h++) {
                joiner.add(sentence[h+pos]);
            }
            joined = joiner.toString();
            output = execute(defaultBrowser+defaultSearch+joined);
            if (output) {
                return "Searching...";
            } else {
                return "ERROR";
            }
        } else if (words[i][0].equals("how")) { //how questions
            StringJoiner joiner = new StringJoiner(" ");
            for (h=0;h<(sentence.length-pos);h++) {
                joiner.add(sentence[h+pos]);
            }
            joined = joiner.toString();
            output = execute(defaultBrowser+defaultSearch+joined);
            if (output) {
                return "Searching...";
            } else {
                 return "ERROR";
            }
        } else if (words[i][0].equals("means")) { //means verb
            String ref = sentence[pos+1];
            String def = sentence[pos-1];
            String line = null;
            String newWords = "";
            int lineInt = 0;
            int foundInt = 0;
            boolean found = false;
            try {
                FileReader filereader = new FileReader(defaultDir+"Dict.java");
                StringJoiner joiner = new StringJoiner("");
                BufferedReader bufferedreader = new BufferedReader(filereader);
                BufferedWriter bw = null;
                FileWriter fw = null;
                while((line = bufferedreader.readLine()) != null) {
                    if (line.contains(ref)) {
                        line = line.replace("},","");
                        joiner.add(line+",\""+def+"\"},\n");
                        foundInt = lineInt-3;
                        found = true;
                    } else {
                        joiner.add(line+"\n");
                    }
                    lineInt++;
                }
                newWords = joiner.toString();
                try {
                    fw = new FileWriter(defaultDir+"Dict.java");
                    bw = new BufferedWriter(fw);
                    bw.write(newWords);
                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bufferedreader.close();
            } catch(FileNotFoundException ex) {
                System.out.println("'"+defaultDir+"Dict.java+"+"' not found!");
            } catch (IOException ex) {
                System.out.println("ERROR");
            }
            if (found) {
                output = execute("javac#s#"+defaultDir+"Dict.java");
                if (output) {
                    /**
                    thisFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    thisFrame.dispose();
                    JFrame thisFrame2 = new JFrame("CBC 1.4");
                    createAndShowCBC(thisFrame2);
                    return "";
                    **/
                    return "Please restart the program!";
                } else {
                     return "ERROR";
                }
            } else {
                return "The word '"+ref+"' is not found!";
            }
        }
        //phrases
        return Integer.toString(pos);
    }
    String stringMath(String text) {
        if (text.contains("+")||text.contains("-")||text.contains("*")||text.contains("/")) {
            int num1 = -1;
            int num2 = -1;
            int i;
            int h;
            int k;
            boolean isNum = false;
            for (i=0;i<text.length();i++) {
               if (String.valueOf(text.charAt(i)).equals("+")||String.valueOf(text.charAt(i)).equals("-")||String.valueOf(text.charAt(i)).equals("*")||String.valueOf(text.charAt(i)).equals("/")) {
                   StringJoiner num1joiner = new StringJoiner("");
                   for (h=0;h<i;h++) {
                       if (isStringNumber(String.valueOf(text.charAt(i-1-h)))) {
                           num1joiner.add(String.valueOf(text.charAt(i-1-h)));
                       } else {
                           break;
                       }
                   }
                   String num1String = num1joiner.toString();
                   StringJoiner revJoiner = new StringJoiner("");
                   for (h=(num1String.length()-1);h>=0;h--) {
                       revJoiner.add(String.valueOf(num1String.charAt(h)));
                   }
                   String revNum1String = revJoiner.toString();                   
                   num1 = Integer.valueOf(revNum1String);
                   StringJoiner num2joiner = new StringJoiner("");
                   for (h=0;h<(text.length()-i-1);h++) {
                       if (isStringNumber(String.valueOf(text.charAt(i+1+h)))) {
                           num2joiner.add(String.valueOf(text.charAt(i+1+h)));
                       } else {
                           break;
                       }
                   }
                   num2 = Integer.valueOf(num2joiner.toString());
                   System.out.println(num1);
                   System.out.println(num2);
               }
            }
            if (num1!=-1&&num2!=-1) {
                if (text.contains("+")) {
                    return Double.toString(num1+num2);
                } else if (text.contains("-")) {
                    return Double.toString(num1-num2);
                } else if (text.contains("/")) {
                    return Double.toString(num1/num2);
                } else if (text.contains("*")) {
                    return Double.toString(num1*num2);
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
        return "";
    }
    String cut(String[] firstString,int cutOff) {
      StringJoiner joiner = new StringJoiner(" ");
      for (int h=0;h<(firstString.length-cutOff);h++) {
            joiner.add(firstString[h+cutOff]);
      }
      String theString = joiner.toString();
      return theString;
    }
    void listFilesForFolder(ArrayList<String> arr,final File folder) {
      arr.clear();
      for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(arr,fileEntry);
            } else {
                arr.add(fileEntry.getName());
            }
        }
    }
    int[] match(String[] arr1, String[][] arr2) {
        int g;
        int k;
        int h;
        int [] arr3 = {-1,-1};
        for (g=0;g<arr1.length;g++) { //for each word in the sentence
            for (h=0;h<arr2.length;h++) { //for each type of word
                for (k=0;k<arr2[h].length;k++) { //for each synonym of word
                    if (arr1[g].toLowerCase().equals(arr2[h][k])) {
                        int[] arr4 = {g,h};
                        return arr4;
                    }
                }
            }
        }
        return arr3;
    }
    boolean execute(String g) {
        String[] command = g.split("#s#");
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            pb.start();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    void musicStart(String file) {
        try {
            InputStream sound = new FileInputStream(file);
            AudioStream music = new AudioStream(sound);
            AudioPlayer.player.start(music);
        } catch(FileNotFoundException e) {
                System.out.println(file+" not found!");
        } catch(Exception e) {
          System.out.println("ERROR method musicStart");
        }
    }
    void changeDefaults(String changing, String changeTo) {
      if (changing.equals("user")) {
          user = changeTo;
      } else if (changing.equals("comp")) {
          compName = changeTo;
      } else if (changing.equals("browser")) {
          StringJoiner joiner = new StringJoiner("");
          joiner.add(changeTo);
          joiner.add("#s#");
          changeTo=joiner.toString();
          changeTo.replace(" ","#s#");
          defaultBrowser = changeTo;
      } else if (changing.equals("editor")) {
          StringJoiner joiner = new StringJoiner("");
          joiner.add(changeTo);
          joiner.add("#s#");
          changeTo=joiner.toString();
          changeTo.replace(" ","#s#");
          defaultEditor = changeTo;
      } else if (changing.equals("search")) {
          defaultSearch = changeTo;
      } else if (changing.equals("directory")) {
          defaultDir = changeTo;
      } else if (changing.equals("player")) {
          StringJoiner joiner = new StringJoiner("");
          joiner.add(changeTo);
          joiner.add("#s#");
          changeTo=joiner.toString();
          defaultPlayer = changeTo;
      }
      BufferedWriter bw = null;
      FileWriter fw = null;
      try {
            fw = new FileWriter(config);
            bw = new BufferedWriter(fw);
            bw.write(videoDir+"\n"+musicDir+"\n"+defaultBrowser+"\n"+user+"\n"+defaultEditor+"\n"+defaultDir+"\n"+compName+"\n"+defaultSearch+"\n"+defaultPlayer);
            bw.close();
            fw.close();
      } catch (IOException e) {
            e.printStackTrace();
      }                                                                                                                                                                                                                                                                                                                                                     
    }
    void reload() {
        String line = null;
        int lineInt = 0;
        try {
            FileReader filereader = new FileReader(config);
            BufferedReader bufferedreader = new BufferedReader(filereader);
            while((line = bufferedreader.readLine()) != null) {
                if (lineInt == 0) {
                    videoDir = line;
                    lineInt++;
                } else if(lineInt == 1) {
                    musicDir = line;
                    lineInt++;
                } else if(lineInt == 2) {
                    defaultBrowser = line;
                    lineInt++;
                } else if(lineInt == 3) {
                    user = line;
                    lineInt++;
                } else if(lineInt == 4) {
                    defaultEditor = line;
                    lineInt++;
                } else if(lineInt == 5) {
                    defaultDir = line;
                    lineInt++;
                } else if (lineInt == 6) {
                    compName = line;
                    lineInt++;
                } else if (lineInt == 7) {
                    defaultSearch = line;
                    lineInt++;
                } else if (lineInt == 8) {
                    defaultPlayer = line;
                    lineInt++;
                }
            }
            bufferedreader.close();
        } catch(FileNotFoundException ex) {
            System.out.println("'"+config+"' not found!");
        } catch (IOException ex) {
            System.out.println("ERROR");
        }
    }
    String mostMatched (String str, ArrayList<String> AL) {
        if (AL.size() != 0) {
            String[] arr = str.split("\\s+");
            int[] list;
            list = new int[AL.size()];
            int numRight = 0;
            int i;
            int h;
            int k;
            for (i=0;i<AL.size();i++) { //for each list element
                String[] arr2 = AL.get(i).split("\\s+");
                for (h=0;h<arr2.length;h++) { //for each word of element
                    String withoutEx = arr2[h];
                    if (arr2[h].contains(".mp3")) {
                        withoutEx = arr2[h].replace(".mp3","");
                    } else if (arr2[h].contains(".mp4")) {
                        withoutEx = arr2[h].replace(".mp4","");
                    } else if (arr2[h].contains(".webm")) {
                        withoutEx = arr2[h].replace(".webm","");
                    } else if (arr2[h].contains(".wav")) {
                        withoutEx = arr2[h].replace(".wav","");
                    }
                    for (k=0;k<arr.length;k++) { // for each word of sentence
                        if (arr[k].toLowerCase().equals(withoutEx.toLowerCase())) {
                            numRight++; 
                        }
                    }
                }
                list[i] = numRight;
                numRight=0;
            }
            int highest = list[0];
            for (i=0;i<list.length;i++) {
                if (i!=(list.length-1)) {
                    if (highest<list[i+1]) {
                        highest = list[i+1];
                    }            
                }
            }
            for (i=0;i<list.length;i++) {
               if (list[i]!=0) {
                  break;
               }
               if (i==(list.length-1)) {
                   return "";
               }
            }
            for (i=0;i<list.length;i++) {
                if (list[i]==highest) {
                    break;
                }
            }
            return AL.get(i);
        } else {
            return "";
        }
    }
    boolean isStringNumber(String num) {
        try {
            Integer.valueOf(num);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    String arrMatch(String[] arr1, String[] arr2) {
        int i;
        int g;
        for (i=0;i<arr1.length;i++) {
            for (g=0;g<arr2.length;g++) {
                if (arr1[i].equals(arr2[g])) {
                    return arr1[i];
                }
            }
        }
        return null;
    }
}