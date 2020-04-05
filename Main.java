import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class HashTag {
    String tag;
    boolean prior;

    HashTag(boolean s, String t) {
        prior = s;
        tag = t;
    }
}

public class Main {
    public static void main(String[] args){
        Redactor r = new Redactor();
        r.ReadAll("text.txt","tags.txt");
        r.Redact();
        r.Write("result.txt");
    }
}

class Redactor{

    public int Max;
    int count=0;
    ArrayList<HashTag> tags = new ArrayList<>();
    ArrayList<String> text = new ArrayList<>();

    public void ReadAll(String fileText, String fileTags){

        Scanner scannerConsole = new Scanner(System.in);                //считываем максимальное количество хештегов
        System.out.println("Введите максимальное количество хештегов в тексте: ");
        Max=scannerConsole.nextInt();

        try {                               //считываем хештеги в ArrayList
            FileReader fr = new FileReader(fileTags);
            Scanner scan = new Scanner(fr);
            String tag;
            boolean prior;
            while (scan.hasNext()) {
                prior=false;
                tag=scan.nextLine();
                if (tag.charAt(tag.length()-1)=='*'){
                    prior=true;
                    tag=tag.substring(0,tag.length()-1);
                }
                tags.add(new HashTag(prior,tag));
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {                                //считываем текст в ArrayList
            FileReader fr = new FileReader(fileText);
            Scanner scan = new Scanner(fr);
            while (scan.hasNext()) {
                text.add(scan.nextLine());
            }
            fr.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void Redact() {

        StringBuilder tagsToEndOfFile = new StringBuilder();//проставляем обязательные хештеги и записываем их еще и в конец файла
        boolean in_text=false;
        for (int i = 0; i < tags.size(); i++) {
            for (int j = 0; j < text.size(); j++) {
                in_text=false;
                if (count < Max && tags.get(i).prior && text.get(j).contains(" " + tags.get(i).tag)) {
                    text.set(j, text.get(j).replaceFirst(" " + tags.get(i).tag, " #" + tags.get(i).tag));
                    count++;
                    in_text=true;
                    break;
                }
            }
            if (count < Max && in_text!=true && tags.get(i).prior) {
                tagsToEndOfFile.append("#" + tags.get(i).tag + " ");
                count++;
            }
        }
        text.add(tagsToEndOfFile.toString());      //добавляем в конец все обязательные хештеги

        for(int k = count; k < Max; k++) {                    //проходим по тексту и расставляем необязательные хештеги
            for (int i = 0; i < tags.size(); i++) {             //каждый по одному разу и потом снова
                for (int j = 0; j < text.size(); j++) {
                    if (count < Max && text.get(j).toLowerCase().contains(" " + tags.get(i).tag)) {
                        text.set(j, text.get(j).replaceFirst(" "+tags.get(i).tag, " #" + tags.get(i).tag));
                        count++;
                        break;
                    }
                }
                if (count >= Max) {
                    break;
                }
            }
            if (count >= Max) {
                break;
            }
        }

    }

    public void Write(String filename){
        try {                                       //записываем получившийся текст с хештегами в результирующий файл
            FileWriter fw = new FileWriter(filename);
            for(int i=0;i<text.size();i++) {
                fw.write(text.get(i)+"\n");
                System.out.println(text.get(i)+"\n");
            }
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}