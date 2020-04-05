import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HashTags {
    public static int N;

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("tags.txt");
            Scanner scan = new Scanner(fis);
            N = scan.nextInt();
        } catch (IOException e) {
            e.getMessage();
        }
        Editor editor = new Editor();
        editor.Run("text.txt", "tags.txt", N);
    }
}

class Editor {
    public void Run(String filename, String fileWithTags, int N) {
        ArrayList<Tag> Tag = ReadTags(fileWithTags);
        ArrayList<String> Lines = ReadText(filename);
        int count = 0;
        boolean flag;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Tag.size(); i++) {
            if (count < N) {
                flag = false;
                for (int j = 0; j < Lines.size(); j++) {
                    if ((Tag.get(i).need) && !flag && (Lines.get(j).contains(" " + Tag.get(i).tag))) {
                        Lines.set(j, Lines.get(j).replaceFirst(" " + Tag.get(i).tag, " #" + Tag.get(i).tag));
                        flag = true;
                        count++;
                    }
                }
                if ((Tag.get(i).need) && !flag) {
                    stringBuilder.append("#" + Tag.get(i).tag + " ");
                    count++;
                }
            }
        }
        Lines.add(stringBuilder.toString());

        for (int k = count; k < N; k++) {
            for (int i = 0; i < Tag.size(); i++) {
                for (int j = 0; j < Lines.size(); j++) {
                    if (count < N && Lines.get(j).toLowerCase().contains(" " + Tag.get(i).tag)) {
                        Lines.set(j, Lines.get(j).replaceFirst(" " + Tag.get(i).tag, " #" + Tag.get(i).tag));
                        count++;
                        break;
                    }
                }
            }
            if (count >= N) {
                break;
            }
        }

        try {
            FileWriter fw = new FileWriter("result.txt");
            for (int i = 0; i < Lines.size(); i++) {
                fw.write(Lines.get(i) + "\n");
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Tag> ReadTags(String filename) {
        ArrayList<String> stringTagsList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            Scanner scan = new Scanner(fis);
            scan.nextLine();
            while (scan.hasNext()) {
                stringTagsList.add(scan.nextLine());
            }
            fis.close();
        } catch (IOException e) {
            e.getMessage();
        }

        ArrayList<Tag> tags = new ArrayList<>();

        for (int i = 0; i < stringTagsList.size(); i++) {
            String tag[] = stringTagsList.get(i).split(",");
            tags.add(new Tag(tag[0].trim(), Boolean.parseBoolean(tag[1])));
        }
        return tags;
    }

    private static ArrayList<String> ReadText(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filename);
            Scanner scan = new Scanner(fis);
            while (scan.hasNext()) {
                lines.add(scan.nextLine());
            }
            fis.close();
        } catch (IOException e) {
            e.getMessage();
        }
        return lines;
    }

}

class Tag {
    String tag;
    boolean need;

    Tag(String n, boolean s) {
        tag = n;
        need = s;
    }
}


