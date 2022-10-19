import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.*;
import java.lang.Math;

public class App extends JFrame implements WindowListener
{ 
    //Static elements 
    static JLayeredPane dealerCards = new JLayeredPane();
    static JLayeredPane playerCards = new JLayeredPane();
    static JPanel titleCard = new JPanel();
    static JButton hitButton = new JButton("Hit");
    static JButton standButton = new JButton("Stand");
    ArrayList<ImageIcon> cards = new ArrayList<ImageIcon>();
    static BufferedImage img;   
    static JLabel gameEnd = new JLabel("Game End");

    //Utility variables
    Color paneColor = new Color(0,0,139);
    

    App()
    {
        //Format Window
        setSize(750,500);
        setVisible(true);
        setLayout(null);
        setResizable(false);
        setTitle("Blackjack");
        addWindowListener(this);
        getContentPane().setBackground(new Color(0,80,0));
        

        //Format Panels
        add(dealerCards);
        add(playerCards);

        dealerCards.setBackground(paneColor);
        dealerCards.setBounds(50,50,650,100);
        dealerCards.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1, Color.WHITE, Color.GRAY), "Dealer Cards", 1, 2, new Font("DejaVu Serif", Font.BOLD, 10), Color.WHITE));
        dealerCards.setOpaque(true);

        playerCards.setBackground(paneColor);
        playerCards.setBounds(50,300,650,100);
        playerCards.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1, Color.WHITE, Color.GRAY), "Player Cards", 1, 2, new Font("DejaVu Serif", Font.BOLD, 10), Color.WHITE));
        playerCards.setOpaque(true);

        //Format Game End Stuff
        gameEnd.setFont(new Font("DejaVu Serif", Font.BOLD, 30));
        gameEnd.setForeground(Color.WHITE);
        gameEnd.setBackground(new Color(159,0,15));
        gameEnd.setOpaque(true);
        gameEnd.setHorizontalAlignment(SwingConstants.CENTER);
        gameEnd.setBounds(225,25,210,50);

        //Set up Cards
        try
        {
        img = ImageIO.read(new File("src\\8BitDeckAssets.png"));
        }
        catch(IOException e)
        {
            System.out.println("Image not found");
        }

        //Change frame icon (needs to be here since it pulls from the same image as cards)
        setIconImage(img.getSubimage(50,148,10,10));

        //Title 
        titleCard.setBounds(190,175,400,50);
        titleCard.setLayout(null);
        titleCard.setOpaque(false);

        JLabel heart = new JLabel("♥");
        heart.setBounds(0,0,200,50);
        heart.setFont(new Font("DejaVu Serif", Font.BOLD, 40));
        heart.setForeground(Color.RED);
        titleCard.add(heart);

        JLabel clover = new JLabel("♣");
        clover.setBounds(50,0,50,50);
        clover.setFont(new Font("DejaVu Serif", Font.BOLD, 40));
        clover.setForeground(Color.BLACK);
        titleCard.add(clover);

        JLabel diamond = new JLabel("♦");
        diamond.setBounds(315,0,50,50);
        diamond.setFont(new Font("DejaVu Serif", Font.BOLD, 40));
        diamond.setForeground(Color.RED);
        titleCard.add(diamond);

        JLabel spade = new JLabel("♠");
        spade.setBounds(365,0,50,50);
        spade.setFont(new Font("DejaVu Serif", Font.BOLD, 40));
        spade.setForeground(Color.BLACK);
        titleCard.add(spade);

       
        JLabel title = new JLabel("BlackJack");
        title.setBounds(88,0,225,50);
        title.setFont(new Font("DejaVu Serif", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        titleCard.add(title);
        add(titleCard);

        //Start Button (also Play Again Button)
        JButton startButton = new JButton("Start");
        startButton.setBounds(325,250,100,25);
        add(startButton);
        startButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //remove(title);
                remove(startButton);
                startButton.setText("Play Again");
                add(hitButton);
                add(standButton);
                dealerCards.removeAll();
                playerCards.removeAll();
                pulledCards.clear();
                playerCardValues.clear();
                dealerCardValues.clear();
                playerCardSum = 0;
                dealerCardSum = 0;
                repaint();
                for(int i=0; i<2;i++)
                {
                    pullCard(dealerCards);
                    pullCard(playerCards);
                }
            }
        });

        //Game Buttons
        hitButton.setBounds(200,250,100,25);
        hitButton.addActionListener(new ActionListener()
        {
            //Hit Function
            public void actionPerformed(ActionEvent e)
            {
                pullCard(playerCards);
                //Check for busts or blackjacks
                if(playerCardSum > 21)
                {
                    System.out.println("Player bust");
                    remove(hitButton);
                    remove(standButton);
                    gameEnd.setText("Player Bust");
                    playerCards.add(gameEnd,0);
                    add(startButton,0);
                    repaint();
                }
                else if(playerCardSum == 21)
                {
                    System.out.println("Player blackjack");
                    remove(hitButton);
                    remove(standButton);
                    gameEnd.setText("Blackjack");
                    playerCards.add(gameEnd,0);
                    add(startButton,0);
                    repaint();
                }
            }
        });

        standButton.setBounds(450,250,100,25);
        standButton.addActionListener(new ActionListener()
        {
            //Stand Function
            public void actionPerformed(ActionEvent e)
            {
                while(dealerCardSum < playerCardSum)
                {
                    pullCard(dealerCards);
                    //Check for busts, wins, and blackjacks
                    if(dealerCardSum>playerCardSum && dealerCardSum<21) //Dealer Wins
                    {
                        System.out.println("Dealer win");
                        remove(hitButton);
                        remove(standButton);
                        gameEnd.setText("Dealer Wins");
                        dealerCards.add(gameEnd,0);
                        add(startButton);
                        repaint();
                    }
                    else if(dealerCardSum > 21) //Dealer Bust
                    {
                        System.out.println("Dealer bust");
                        remove(hitButton);
                        remove(standButton);
                        gameEnd.setText("Dealer Bust");
                        dealerCards.add(gameEnd,0);
                        add(startButton,0);
                        repaint();
                    }
                    else if(dealerCardSum == 21)  //Dealer Blackjack
                    {
                        System.out.println("Dealer blackjack");
                        remove(hitButton);
                        remove(standButton);
                        gameEnd.setText("Blackjack");
                        dealerCards.add(gameEnd,0);
                        add(startButton,0);
                        repaint();
                    }
                }

                if(dealerCardSum > playerCardSum && dealerCardSum < 21) //Player stands with a lower hand
                {
                    System.out.println("Dealer win");
                    remove(hitButton);
                    remove(standButton);
                    gameEnd.setText("Dealer Wins");
                    dealerCards.add(gameEnd,0);
                    add(startButton,0);
                    repaint();
                }
                else if(dealerCardSum == playerCardSum) //Push
                {
                    System.out.println("Push");
                    remove(hitButton);
                    remove(standButton);
                    gameEnd.setText("Push");
                    dealerCards.add(gameEnd,0);
                    add(startButton,0);
                    repaint();
                }
            }
        });
    }
    

    //Card Pulling Function

    public static ArrayList <int[]> pulledCards = new ArrayList<int[]>();
    public static ArrayList <Integer> playerCardValues = new ArrayList<Integer>();
    public static ArrayList <Integer> dealerCardValues = new ArrayList<Integer>();
    public static int playerCardSum = 0;
    public static int dealerCardSum = 0;

    public static void pullCard(JLayeredPane frameToAdd)
    {
        boolean cardIsRepeat = true;
        int[]card = new int[2];

        do
        {
            cardIsRepeat = false;

            //Generate random card
            card[0] = (int)(Math.random()*13);; //Card value, 0 is 2, 1 is 3,... 9 is J, 10 is Q, 11 is K, 12 is A
            card[1] = (int)(Math.random()*4); //Card suit, 0 is hearts, 1 is clovers, 2 is diamonds, 3 is spades

            //Check if card is already pulled
            for(int i=0; i<pulledCards.size(); i++)
            {
                if(Arrays.equals(pulledCards.get(i), card))
                {
                    cardIsRepeat=true;
                }
            }
        }
        while(cardIsRepeat);
        pulledCards.add(card);

        //Pull image from spritesheet
        JLabel returnCard = new JLabel(new ImageIcon((img.getSubimage(35+(35*card[0]),0+(47*card[1]),35,47)).getScaledInstance(45,69,Image.SCALE_DEFAULT))); //Cards on spritesheet are 35 x 47
        
        //Add value to respective array, set up bounds, add to frame, and update
        if(frameToAdd.equals(playerCards) && playerCardValues.size()<11)
        {
            if(card[0]<8) //if card is not a face or ace
            {
                playerCardValues.add(card[0] + 2);
                playerCardSum += (card[0] + 2);
            }
            else if(card[0]<12) //if card is not an ace (i.e. is a face)
            {
                playerCardValues.add(10);
                playerCardSum += 10;
            }
            else //card must be an ace
            {
                playerCardValues.add(1);
                playerCardSum += 1;
            }

            playerCards.add(returnCard);
            playerCards.repaint();
            returnCard.setBounds((playerCardValues.size()*50),25,40,65);
        }
        else if(frameToAdd.equals(dealerCards) && dealerCardValues.size()<11)
        {
            if(card[0]<8) //if card is not a face or ace
            {
                dealerCardValues.add(card[0] + 2);
                dealerCardSum += (card[0] + 2);
            }
            else if(card[0]<12) //if card is not an ace (i.e. is a face)
            {
                dealerCardValues.add(10);
                dealerCardSum += 10;
            }
            else //card must be an ace
            {
                dealerCardValues.add(1);
                dealerCardSum += 1;
            }

            dealerCards.add(returnCard);
            dealerCards.repaint();
            returnCard.setBounds((dealerCardValues.size()*50),25,40,65);
        }
    }

    //Window Listener Functions
    public void windowClosing(WindowEvent e)
    {
        dispose();
        System.exit(0);
    }
    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public static void main(String[] args) throws Exception 
    {
        new App();
    }
}
