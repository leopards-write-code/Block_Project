//Name - Victor Li
//Date - 4/03/25
//Class - Computer Science 2 K
//Lab  - Exploding Block Project
/*
       DESCRIPTION: Write a graphical program BlocksMain.java that draws moving blocks on the screen. The
graphical user interface allows the user to interact through key presses and mouse clicks as
outlined in this documentation. When the program runs, 20 blocks should be displayed on the
screen (stored in a blocks list).The graphical user interface displays the blocks and allows the user to manipulate them.
 */



import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BlocksMain extends JFrame implements ActionListener, MouseListener {
    /*
        The BlocksMain class is where all the code that
        draws the shapes and allows the user to
        interact with the simulation is located.
     */
    private Timer timer;
    private Timer explodingTimer;
    private JPanel drawingPanel;

    private JPanel speedControlsPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JButton start = new JButton("start");
    private JButton stop= new JButton("stop");
    private JButton step=new JButton("step");
    private JButton reset = new JButton("reset");
    private JButton music = new JButton("music");
    private JButton quit = new JButton("quit");
    private JButton save = new JButton("SAVE\uD83D\uDCBC");

    private JRadioButton defaultSpeed = new JRadioButton("Default");
    private JRadioButton extraSlow = new JRadioButton("Slow ");
    private JRadioButton fast = new JRadioButton("Fast");
    private JRadioButton extraFast = new JRadioButton("\uD83D\uDC06ྀི");


    private final int PANELWIDTH = 800;
    private final int PANELHEIGHT = 600;

    private ArrayList<JButton> buttons;
    private ArrayList<JRadioButton>speedControls;


    private ButtonGroup bg;
    private Clip clip;
    private boolean explodingOnly;
    private boolean isRunning;
    private boolean musicOn;
    private BlockManager bm;

    // Had to look up HashSet because it's
    // a data structure that only allows unique elements
    // so arraylist wouldn't work since duplicate keys would be added
    private Set<Integer> pressedKeys = new HashSet<>();
    public BlocksMain() {
        setTitle("Designed by \uD83C\uDD86\uD83C\uDD7E\uD83C\uDD7D\uD83C\uDD73\uD83C\uDD74\uD83C\uDD81 \uD83C\uDD70\uD83C\uDD7F\uD83C\uDD7F\uD83C\uDD7B\uD83C\uDD78\uD83C\uDD72\uD83C\uDD70\uD83C\uDD83\uD83C\uDD78\uD83C\uDD7E\uD83C\uDD7D\uD83C\uDD82"+" 2025©");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //centers the frame
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) (.5*(screensize.width-getWidth())), (int)(.5*(screensize.height-getHeight())), getWidth(), getHeight() );
        setResizable(false);
        initGUI();
        pack();
    }
    private void initGUI(){

        addPanels();
        addButtons();
        addSpeedControls();
        addButtonListeners();
        addKeyListener();
        prepareBlockManager();
        setUpExportButtons();
    }
    // Private helper method that adds panels to the frame
    private void addPanels(){
        drawingPanel = new DrawingPanel();

        drawingPanel.setBackground(Color.BLACK);
        drawingPanel.setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));

        getContentPane().add(drawingPanel, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
    }
    // Prepares the main buttons (excluding speed)
    private void addButtons(){
        buttons = new ArrayList<>();
        buttons.add(start);
        buttons.add(stop);
        buttons.add(step);
        buttons.add(reset);
        buttons.add(music);
        buttons.add(quit);
        start.setBackground(Color.GREEN);
        stop.setBackground(Color.RED);
        step.setBackground(Color.YELLOW);
        reset.setBackground(Color.CYAN);
        music.setBackground(Color.PINK);
        quit.setBackground(Color.ORANGE);
        save.setBackground(Color.orange);

    }
    // Controls the speed of explosion/speed of exploding blocks
    // NOT the speed the regular blocks move
    private void addSpeedControls(){
        speedControls = new ArrayList<>();
        bg = new ButtonGroup();
        speedControls.add(extraSlow);
        speedControls.add(defaultSpeed);
        speedControls.add(fast);
        speedControls.add(extraFast);
        for(JRadioButton b: speedControls){
            bg.add(b);
        }
        extraSlow.setBackground(Color.PINK);
        defaultSpeed.setBackground(new Color(225, 190, 231));
        fast.setBackground(new Color(126, 87, 194));
        extraFast.setBackground(new Color(121, 134, 203));
        JLabel label = new JLabel();
        label.setText("EXPLOSION SPEED");
        label.setForeground(Color.WHITE);
        speedControlsPanel.setBackground(Color.BLUE);
        speedControlsPanel.add(label);
        speedControlsPanel.add(extraSlow);
        speedControlsPanel.add(defaultSpeed);
        defaultSpeed.setSelected(true);
        speedControlsPanel.add(fast);
        speedControlsPanel.add(extraFast);
        southPanel.add(speedControlsPanel);
    }
    /*
    Removes any previous button listeners, if any and then
    adds new action listener
     */
    private void addButtonListeners(){
        for(JButton b: buttons){
            southPanel.add(b);
        }
        for(JButton b: buttons){
            for (ActionListener listener : b.getActionListeners()) {
                b.removeActionListener(listener);
            }
        }
        for (JButton b : buttons) {
            b.addActionListener(this);
        }
        for(JRadioButton b: speedControls){
            for(ActionListener listener : b.getActionListeners()){
                b.removeActionListener(listener);
            }
        }
        extraSlow.addActionListener(e -> {
            if (extraSlow.isSelected()) {
                bm.setSpeedOfExplosion(10);
                repaint();
                drawingPanel.requestFocusInWindow();
            }
        });
        defaultSpeed.addActionListener(e -> {
            if(defaultSpeed.isSelected()){
                bm.setSpeedOfExplosion(15);
                repaint();
                drawingPanel.requestFocusInWindow();
            }
        });
        fast.addActionListener(e -> {
            if(fast.isSelected()){
                bm.setSpeedOfExplosion(30);
                repaint();
                drawingPanel.requestFocusInWindow();
            }
        });
        extraFast.addActionListener(e -> {
            bm.setSpeedOfExplosion(50);
            repaint();
            drawingPanel.requestFocusInWindow();
        });
        addMouseListener(this);
        save.addActionListener(this);
        setFocusable(true);
    }
    // This method is called if the user plays music
    private void playMusic(File f){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            long startPos = 5000000;

            clip.setMicrosecondPosition(startPos);
            clip.setLoopPoints((int) startPos, -1);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(UnsupportedAudioFileException e){
            System.out.println("The audio file is not supported");

        }catch(LineUnavailableException e){
            System.out.println("There are issues with this system's audio resources");

        }catch(SecurityException e){
            System.out.println("Make sure the file is accessible and you have proper permissions");

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();  // Stop the music
            clip.close();
        }
    }
    // initializes the block manager
    // adds 20 random blocks
    private void prepareBlockManager(){
        bm = new BlockManager();
        for(int i = 0; i<20; i++){
            bm.addBlock(getRandomBlock());
        }
    }
    private void setUpExportButtons(){
        save.setForeground(new Color(183, 162, 140));
        southPanel.add(save);
    }

    /**
     * Generates a new block
     * with a size from 40 to 120 [inclusive, exclusive]
     * @return randomly created block
     */
    private Block getRandomBlock(){
        int width = (int)(Math.random()*80)+40;
        int height = (int)(Math.random()*80)+40;
        int x = (int)(Math.random()*(PANELWIDTH-width));
        int y = (int)(Math.random()*(PANELHEIGHT-height));
        return new Block(x, y, width, height, getRandomColor());
    }

    private int randomVal(){
        return (int)(Math.random()*236)+20;// ensures it doesn't generate values too dark that will blend in with black
    }
    public Color getRandomColor(){
        return new Color(randomVal(), randomVal(), randomVal());
    }
    public static void main(String[] args){
        new BlocksMain();
    }
    public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
        if(source==start){
            if(explodingOnly){
                explodingTimer.stop();
                explodingOnly = false;
            }
            if(bm.getBlocks().isEmpty()){
                JOptionPane.showMessageDialog(null, "To see, the blocks move, add blocks to the screen by pressing n");
            }
            else if(!isRunning){
                isRunning= true;
                timer = new Timer(40, x->{
                    bm.moveAll(PANELWIDTH, PANELHEIGHT);
                    repaint();
                });
                timer.start();
                SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
            }
            else{
                JOptionPane.showMessageDialog(null, "The blocks are already moving across the screen, if you want to STOP them, then hit the stop button");
                drawingPanel.requestFocusInWindow();
            }
        }
        else if(source == stop){
            if(isRunning){
                isRunning = false;
                timer.stop();
                repaint();
            }
            if(explodingOnly){
                explodingOnly = false;
                explodingTimer.stop();
                repaint();
            }
            drawingPanel.requestFocusInWindow();
        }
        else if(source == step){
            if(isRunning){
                JOptionPane.showMessageDialog(null, "To step, through, you must hit stop first");
                drawingPanel.requestFocusInWindow();
            }
            else{
                bm.moveAll(PANELWIDTH, PANELHEIGHT);
                SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
                repaint();
            }

        }
        else if(source == reset){
            bm.clear();
            save.removeActionListener(this);
            removeMouseListener(this);

            initGUI();

            repaint();
        }
        else if(source == quit){
            int r = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "QUIT",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(JOptionPane.YES_OPTION == r)System.exit(0);
            drawingPanel.requestFocusInWindow();
        }
        else if(source== save){
            saveFile();
            SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
        }
        else{//source == music
            musicOn = !musicOn;
            if(musicOn){
                File soundFile = new File("Nessun_Dorma.wav");
                playMusic(soundFile);
            }
            else{
                stopMusic();
            }

            SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
        }
    }
    public void addKeyListener(){
        drawingPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_N){
                    Block b = getRandomBlock();
                    bm.addBlock(b);
                    repaint();
                    SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
                }
                else if(e.getKeyCode()==KeyEvent.VK_S){
                     bm.shuffle(PANELWIDTH, PANELHEIGHT);
                     repaint();
                    SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());
                }
                else if(e.getKeyCode() == KeyEvent.VK_E){
                    bm.explodeAll();
                    prepExplodingOnlyTimer();
                }
                else if(e.getKeyCode() == KeyEvent.VK_I){
                    bm.implodeAll();
                    prepExplodingOnlyTimer();
                }
                /*\
                    if the key is not already in the set, it
                    will be added, that way, if the user holds
                    down a key it doesn't put it in multiple times
                 */
                pressedKeys.add(e.getKeyCode());

            }
            @Override
            public void keyReleased(KeyEvent e) {
                // removes the key from a set
                pressedKeys.remove(e.getKeyCode());
            }

        });
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        y-=28;
        x-=9;
        /*
            If the user clicks the left mouse button while the mouse cursor points at a tile, that tile is
moved to the very top of the z-ordering (the end of the block list). MouseEvent.BUTTON1,
MouseEvent.BUTTON2 and MouseEvent.BUTTON3 are for left, middle and right click
respectively.
         */

        if(e.getButton()==MouseEvent.BUTTON1&& pressedKeys.contains(KeyEvent.VK_SHIFT)){
             bm.lower(x, y);
             repaint();
        }
        else if(e.getButton()==MouseEvent.BUTTON1){
            bm.raise(x, y);
            repaint();
        }
        else if(e.getButton()==MouseEvent.BUTTON3 && pressedKeys.contains(KeyEvent.VK_SHIFT)){
            bm.deleteAll(x, y);
            repaint();
        }
        else if(e.getButton()==MouseEvent.BUTTON3){
            bm.delete(x, y);
            repaint();

        }
        else if(e.getButton()==MouseEvent.BUTTON2){
             //all blocks that occupy that pixel are exploded
            bm.explode(x, y);
            prepExplodingOnlyTimer();
        }
        SwingUtilities.invokeLater(() -> drawingPanel.requestFocusInWindow());

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    /*
        If the user explodes a block while !isRunning
        and the regular blocks aren't moving, this method
        will call a method to move the exploding blocks only.
        Regular moveAll won't work because it would move the non
        exploding blocks too.
     */
    private void prepExplodingOnlyTimer(){
        SwingUtilities.invokeLater(()->drawingPanel.requestFocusInWindow());
        if(!isRunning&&!explodingOnly){
            explodingOnly = true;
            explodingTimer = new Timer(40, z->{
                bm.moveExplodingOnly(PANELWIDTH, PANELHEIGHT);
                repaint();
            });
            explodingTimer.start();
        }


        repaint();
    }
    private void saveFile(){
        // Create a BufferedImage to capture the panel's content

        BufferedImage image = new BufferedImage(southPanel.getWidth(), southPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // grab the stuff on the drawing panel and draw it on the image for the user
        drawingPanel.paint(image.getGraphics());
        JFileChooser fileSelector = new JFileChooser();
        fileSelector.setDialogTitle("Select this file to rename it");

        int result = fileSelector.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION){
            File f = fileSelector.getSelectedFile();
            allowUserToRename(f);
        }
    }
    private void allowUserToRename(File f){
        String currentFileName = f.getName();

        String fileNameNew = JOptionPane.showInputDialog(null, "Enter a new name for the file ఠ_ఠ:", currentFileName);
        if(fileNameNew!=null&&!fileNameNew.isEmpty()){
            File renamedF = new File(f.getParent(), fileNameNew);
            boolean renamed = f.renameTo(renamedF);
            if(renamed){
                JOptionPane.showMessageDialog(null, "File renamed! :>)");
            }
            else{
                JOptionPane.showMessageDialog(null, "Sorry, there was an issue renaming the file. :>(");
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Please make sure to fill out the name field (✖╭╮✖)");
        }
    }
    private class DrawingPanel extends JPanel{
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            bm.drawAll(g);
        }
    }
}
