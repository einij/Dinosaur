import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Dinosaur class to handle dinosaur attributes and abilities
public class Dinosaur {
    protected String filename;
    private String name;
    private int height, width;
    private int jumpHeight, secondJumpHeight;
    private float gravity;  // 0 < gravity < 10

    protected static String PATH_PREFIX = "data/";

    public Dinosaur(String filename) {
        this.filename = filename;
        this.name = filename.split("/")[1].split("\\.")[0];
        this.name = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
         loadDinosaurData();
    }

    private void loadDinosaurData()
    {
        File file = new File(filename);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] values = data.split(" ");
                if (values.length == 5) {
                    this.height = Integer.parseInt(values[0]);
                    this.width = Integer.parseInt(values[1]);
                    this.jumpHeight = Integer.parseInt(values[2]);
                    this.secondJumpHeight = Integer.parseInt(values[3]);
                    this.gravity = Float.parseFloat(values[4]);
                } else {
                    throw new IllegalArgumentException("Data format in file is incorrect. Expected 6 values.");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filename);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number from file: " + filename);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public int getSecondJumpHeight() {
        return secondJumpHeight;
    }

    public float getGravity() {
        return gravity;
    }
}
