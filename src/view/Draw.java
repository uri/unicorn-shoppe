package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import controller.Game;

import model.Item;
import model.ItemFactory;
import model.World;
import utility.Utility;
import view.resources.Images;

public class Draw {
    public static final int IMG_WIDTH = 91;
    public static final int IMG_HEIGHT = 48;
    public static final int IMG_SCALE = 8;
    private static final int IMG_RAW_SIZE = 16;
    public static int BUTTON_WIDTH = (IMG_WIDTH * IMG_SCALE) / ItemFactory.TYPE.length;
    public static int BUTTON_HEIGHT = (IMG_HEIGHT / 3) * IMG_SCALE;
    public static int BUTTON_OFFSET = (2 * IMG_SCALE);
    public static String RESOURCE_LOC = "src" + File.separator + "resources" + File.separator;
    public static final int EXTRA_BUTTON_HEIGHT = 16;
    private BufferedImage background;
    private BufferedImage itemImages[];
    private BufferedImage buttonNormal;
    private BufferedImage buttonPressed;
    private World world;

    public Draw() {

        // Load the image
        int rand = new Random().nextInt(4);
        // System.out.println("This is the random image: " + rand);
        background = getImage("shoppe" + rand);
        buttonNormal = getImage("button_normal");
        buttonPressed = getImage("button_pressed");
        itemImages = new BufferedImage[ItemFactory.ITEM_COUNT];

        // Load the images
        for (int i = 0; i < ItemFactory.ITEM_COUNT; i++) {
            itemImages[i] = getImage(getImageName(ItemFactory.TYPE[i]));
        }

    }

    /**
     * Main draw method calls other private methods; publicly visible
     * @param world
     * @param g
     * @param mouse
     */
    public void draw(World world, Graphics g, int mouse) {
        this.world = world;

        BufferedImage text = Draw.createBlankImage();

        drawImage(g, background);
        drawImage(g, text);
        drawButtons(g, mouse);
        drawMoney(g);

    }

    /**
     * Draws an image
     * @param g
     * @param image
     * @return
     */
    public Graphics drawImage(Graphics g, BufferedImage image) {
        g.drawImage(image, 0, 0, IMG_WIDTH * IMG_SCALE, IMG_HEIGHT * IMG_SCALE, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        return background.getGraphics();
    }

    /**
     * Draws the items
     * @param g
     * @param image
     * @param x
     * @param y
     * @return
     */
    public Graphics drawStockImages(Graphics g, BufferedImage image, int x, int y) {
        return drawImage(g, image, x + BUTTON_OFFSET, y + BUTTON_OFFSET, IMG_RAW_SIZE * 6 / 8);
    }

    /**
     * 
     * @param g The graphics device
     * @param image The intended image to draw
     * @param x he x location of the image
     * @param y The y location of the image
     * @return not used
     */
    public Graphics drawImage(Graphics g, BufferedImage image, int x, int y, int imageSize) {
        g.drawImage(image, x, y, imageSize * IMG_SCALE, imageSize * IMG_SCALE, null);
        return background.getGraphics();
    }

    /**
     * Draw the money string
     * 
     * @param g
     * @return
     */
    public void drawMoney(Graphics g) {

        // Display string
        String toDisp = "$" + Utility.formatTwoDecimal(world.getMoney());

        // Set the font
        g.setFont(new Font("Arial", Font.PLAIN, 7 * IMG_SCALE));

        // Get some font metrics
        FontMetrics metrics = g.getFontMetrics();
        int strWidth = metrics.stringWidth(toDisp);
        int strHeight = metrics.getHeight();

        g.setColor(Color.MAGENTA);
        g.drawString(toDisp, (IMG_WIDTH * IMG_SCALE) - strWidth, (IMG_HEIGHT * IMG_SCALE));

        // Offset a second string to give it that popping effect
        g.setColor(Color.ORANGE);
        g.drawString(toDisp, (IMG_WIDTH * IMG_SCALE) - strWidth - 2, (IMG_HEIGHT * IMG_SCALE) - 2);

    }

    /**
     * This method draws the buttons for the user to click on. The buttons are
     * split up into three layers: the base layer button, the clicked button
     * (not always visible), and the icon of the button.
     * 
     * @param g
     * @param mouse
     *            this is the "location" of the mouse.
     */
    public void drawButtons(Graphics g, int mouse) {

        // Debug
        if (Game.DEBUG_MODE) {
            drawDebug(g);
        }
        
        /**
         * If something is stocking then that should be the only visible button
         * 
         * Once it is done stocking everything else that is currently not
         * stocked or should appear
         * 
         * Everything that is ready to be stocked should be displayed with a
         * "stock!" overlay
         */

        for (int i = 0; i < ItemFactory.ITEM_COUNT; i++) {

            Item currItem = world.getStore().getItems()[i];
            drawBaseButton(g, i);
            drawStockImages(g, itemImages[i], (BUTTON_WIDTH * i), (IMG_HEIGHT) * IMG_SCALE); // Draw the item images

            if (world.getStore().isStocking()) {
                
                if (!currItem.isReady()) {
                    drawDisabledButton(g, i);
                }
                
                
            } else {
                drawClickButton(g, mouse);
//                if (currItem.getAmount() != 0)
//                    drawDisabledButton(g, i);
            }

            
            // Draw quantity of stock for items that have already been stocked
            if (currItem.getAmount() > 0)
                drawStockNumber(g,i);


            if (currItem.isReady())
                drawStockReady(g, i);
            else
                drawTimeRemaining(g, i);             // Draw the time remaining on the item that is stocking
        }
    }



    /**
     * This is convinience method for drawing a button, it is supposed to be
     * used inside the drawButtons method to clean up some code
     * 
     * @param g
     * @param i
     */
    private void drawButton(Graphics g, int i, Color c) {

        // Draw the background
        g.setColor(new Color(195, 199, 164));
        g.fillRoundRect((BUTTON_WIDTH * i) + BUTTON_OFFSET / 2, (IMG_HEIGHT * IMG_SCALE) + BUTTON_OFFSET / 2, BUTTON_WIDTH - BUTTON_OFFSET, BUTTON_HEIGHT
                - BUTTON_OFFSET, 4 * IMG_SCALE, 4 * IMG_SCALE);

        // Draw the border
        g.setColor(c);
        g.fillRoundRect((BUTTON_WIDTH * i) + BUTTON_OFFSET / 2 + IMG_SCALE / 2, (IMG_HEIGHT * IMG_SCALE) + BUTTON_OFFSET / 2 + IMG_SCALE / 2, BUTTON_WIDTH
                - BUTTON_OFFSET - IMG_SCALE, BUTTON_HEIGHT - BUTTON_OFFSET - IMG_SCALE, 4 * IMG_SCALE, 4 * IMG_SCALE);

    }

    private void drawTimeRemaining(Graphics g, int i) {
        
        // If there is no stock
        if (world.getStore().getItems()[i].getStockTime() <= 0)
            return;
        
        // Display string
        String toDisp = (int)Math.ceil(world.getStore().getItems()[i].getStockTime() / 60000) + "m";

        // Set the font
        g.setFont(new Font("Arial", Font.BOLD, 3 * IMG_SCALE));

        // Get some font metrics
        FontMetrics metrics = g.getFontMetrics();
        int strWidth = metrics.stringWidth(toDisp);
        int strHeight = metrics.getHeight();

        g.setColor(Color.MAGENTA);
        g.drawString(toDisp, (i * BUTTON_WIDTH) + BUTTON_WIDTH - (7 * IMG_SCALE) , IMG_HEIGHT * IMG_SCALE + BUTTON_WIDTH /4 );
    }
    
    
    private void drawStockReady(Graphics g, int i) {
        // Display string
        String toDisp = "STOCKED";

        // Set the font
        g.setFont(new Font("Arial", Font.BOLD, 3 * IMG_SCALE));

        // Get some font metrics
        FontMetrics metrics = g.getFontMetrics();
        int strWidth = metrics.stringWidth(toDisp);
        int strHeight = metrics.getHeight();

        g.setColor(Color.MAGENTA);
        g.drawString(toDisp, (i * BUTTON_WIDTH) + BUTTON_WIDTH - (7 * IMG_SCALE) , IMG_HEIGHT * IMG_SCALE + BUTTON_WIDTH /4 );        
    }
    
    private void drawStockNumber(Graphics g, int i) {
    }
    
    public void drawDebug(Graphics g) {
        
        g.setFont(new Font("Arial", Font.BOLD, 3 * IMG_SCALE));
        g.setColor(Color.MAGENTA);

        
     // Display string
        Item[] theItems = world.getStore().getItems();
        
        // Amount
        String toDisp = "Amount:";
        for (int j = 0; j < 5; j++) {
            toDisp += theItems[j].getAmount() + " ";    
        }
        g.drawString(toDisp, 20, 30 ); 
        
         
        // Stock time
        String toDisp2 = "Stock Time:";
        for (int j = 0; j < 5; j++) {
            toDisp2 += theItems[j].getStockTime()/ItemFactory.STOCK_TIME_WEIGHT + " ";    
        }
        g.drawString(toDisp2, 20, 60);
        
        // Ready
        String toDisp3 = "Stock Time:";
        for (int j = 0; j < 5; j++) {
            
            if (theItems[j].isReady()) {
                toDisp3 += "READY" + " ";    
            } else {
                toDisp3 += "NOT" + " ";
            }
                
        }
        g.drawString(toDisp3, 20, 90 ); 
        

    }

    private void drawBaseButton(Graphics g, int i) {
        drawButton(g, i, new Color(178, 200, 165));
    }

    private void drawClickButton(Graphics g, int i) {
        drawButton(g, i, new Color(33, 66, 99));
    }

    private void drawDisabledButton(Graphics g, int i) {
        // Draw the background
        int opacity = 130;
        
        g.setColor(new Color(40, 50, 50, opacity));
        g.fillRoundRect((BUTTON_WIDTH * i) + BUTTON_OFFSET / 2, 
                        (IMG_HEIGHT * IMG_SCALE) + BUTTON_OFFSET / 2, 
                        BUTTON_WIDTH - BUTTON_OFFSET, BUTTON_HEIGHT - BUTTON_OFFSET, 
                        4 * IMG_SCALE, 4 * IMG_SCALE);
    }

    /**
     * Creates a blank buffered image which is the same size as the entire
     * application. This is mainly used for drawing text since we want the text
     * to be on it's own layer. This method might not be needed.
     * 
     * @return not used.
     */
    private static BufferedImage createBlankImage() {
        return new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Returns a File object for the specified string. Looks in the resource
     * folder for a .png image.
     * 
     * @param filename
     *            name of the file.
     * @return
     * @return
     */
    private static String getImageName(String filename) {
        return ItemFactory.TYPE[ItemFactory.TYPE_MAP.get(filename)];
    }

    // TODO: Comment this method
    private BufferedImage getImage(String filename) {
        BufferedImage ret = null;

        try {
            InputStream in = Images.class.getResourceAsStream(filename + ".png");
            if (in == null)
                System.out.println("stream is null");
            ret = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
