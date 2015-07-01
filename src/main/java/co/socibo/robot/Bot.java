package co.socibo.bot;

import java.awt.*;
import java.awt.HeadlessException;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64;
import javax.imageio.ImageIO;


public class Bot {
    private Robot robot;

    public Bot(){
	try{
	    robot = new Robot();
	}catch(AWTException e){
	    e.printStackTrace();
	}
    }

    public void click(final int times, final int button)
	throws AWTException, InterruptedException{
	System.out.println("click called");
	for(int i = 0;i<times;i++){
	    robot.mousePress(button);
	    robot.delay(10);
	    robot.mouseRelease(button);
	    robot.delay(10);
	}
    }

    public void move(int x, int y)
	throws AWTException, InterruptedException{
	System.out.println("move called");
	robot.mouseMove(x, y);
    }

    public void key(final int [] keys)
	throws AWTException, InterruptedException {
	System.out.println("key called");
	    
	for(int i=0;i<keys.length;i++){
	    robot.keyPress(keys[i]);
	    robot.delay(50);
	};
	robot.delay(50);
	for(int i=0;i<keys.length;i++){
	    robot.keyRelease(keys[i]);
	    robot.delay(50);
	}
    }

    public void enter(final String string)
	throws AWTException, InterruptedException {
	System.out.println("enter called");
	    
	for (int i = 0; i < string.length(); i++) {
	    char c = string.charAt(i);
	    if (Character.isUpperCase(c)) {
		robot.keyPress(KeyEvent.VK_SHIFT);
	    }
	    robot.keyPress(Character.toUpperCase(c));
	    robot.keyRelease(Character.toUpperCase(c));

	    if (Character.isUpperCase(c)) {
		robot.keyRelease(KeyEvent.VK_SHIFT);
	    }
	}
	robot.delay(50);
    }
	
    public void scroll(final int y){
	System.out.println("scroll called");	    
	robot.mouseWheel(y);
    }

    private static String imgToBase64String(final RenderedImage img, final String formatName) {
	final ByteArrayOutputStream os = new ByteArrayOutputStream();
	try {
	    ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
	    return os.toString(StandardCharsets.ISO_8859_1.name());
	} catch (final IOException ioe) {
	    throw new UncheckedIOException(ioe);
	}
    }

    private static BufferedImage base64StringToImg(final String base64String) {
	try {
	    return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64String)));
	} catch (final IOException ioe) {
	    throw new UncheckedIOException(ioe);
	}
    }

    public String showScreen(){
	final Rectangle r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	final BufferedImage bi = robot.createScreenCapture(r);
	final String base64String = imgToBase64String(bi, "png");
	return base64String;
    }

    public String send(final String text){
	return text;
    }

    
    public Robot getRobotInstance(){
	return robot;
    }
}
