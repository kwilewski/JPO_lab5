/*import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import javax.swing.JApplet;
import java.awt.geom.*;*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Timer;

public class SpringApplet extends JApplet implements MouseListener, MouseMotionListener, ActionListener {
    private SimEngine engine;
    private SimTask task;
    private Timer timer;
    private boolean mousedrag;
    private double m=20,k=2,c=0.2,l=200,g=9.81;
    private TextField fm,fk,fc,fg,fl;
    private Button butzastosuj,butreset;
    private Label lm,lk,lc,lg,ll;


    public void utwierdzenie(Graphics g,Vector2D xz){       //rysowanie symbolu utwierdzenia
        Graphics2D g2= (Graphics2D) g;
        g2.setColor(Color.gray);
        g2.draw(new Line2D.Double(xz.getWspx()-50,xz.getWspy(),xz.getWspx()+50,xz.getWspy()));
        for(int i=(int)xz.getWspx()-50;i<xz.getWspx()+50;i=i+10){
            g2.draw(new Line2D.Double(i,xz.getWspy(),i+10,xz.getWspy()-20));
        }
    }

    public void sprezyna(Graphics2D g, Vector2D xz, Vector2D x){        //rysowanie liny zastepujacej sprezyne
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.gray);
        g2.draw(new Line2D.Double(xz.getWspx(),xz.getWspy(),x.getWspx(),x.getWspy()));
    }

    //metody konieczne do obsługi myszy
    public void mouseClicked(MouseEvent event){

    }

    public void mousePressed(MouseEvent event){
        Vector2D mysz=new Vector2D(event.getX(),event.getY());
        Vector2D x=this.engine.getXm();
        if(mysz.roznica(x).dlugosc()<=20){
            this.timer.cancel();
            engine.reset();
            Vector2D vzero=new Vector2D(0,0);
            this.engine.setV(vzero);
            this.mousedrag=true;
        }
        event.consume();
    }

    public void mouseReleased(MouseEvent event){
        if(this.mousedrag){
            this.mousedrag=false;
            this.engine = new SimEngine(m, k, c, l, g, this.engine.getXm(), this.engine.getV(), new Vector2D(400, 30));
            this.task = new SimTask(engine, this, 0.1);
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(task, 100, 10);
        }
        event.consume();
    }

    public void mouseEntered(MouseEvent event){
    }

    public void mouseExited(MouseEvent event){
    }

    public void mouseDragged(MouseEvent event){
        if(this.mousedrag){
            Vector2D mysz=new Vector2D(event.getX(),event.getY());
            this.engine.setXm(mysz);
            repaint();
            if(event.getX()>800){
                mysz=new Vector2D(800,event.getY());
                this.engine.setXm(mysz);
                repaint();
            }
            if(event.getX()<1){
                mysz=new Vector2D(1,event.getY());
                this.engine.setXm(mysz);
                repaint();
            }
            if(event.getY()>600){
                mysz=new Vector2D(event.getX(),600);
                this.engine.setXm(mysz);
                repaint();
            }
            if(event.getX()<1){
                mysz=new Vector2D(1,event.getY());
                this.engine.setXm(mysz);
                repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent event){

    }

    //metoda do implementacji ActionListener
    public void actionPerformed(ActionEvent event){
        if(event.getSource()==butzastosuj){
            timer.cancel();
            engine.reset();
            m=Double.parseDouble(fm.getText());
            k=Double.parseDouble(fk.getText());
            c=Double.parseDouble(fc.getText());
            l=Double.parseDouble(fl.getText());
            g=Double.parseDouble(fg.getText());
            this.engine = new SimEngine(m, k, c, l, g, this.engine.getXm(), this.engine.getV(), new Vector2D(400, 30));
            this.task = new SimTask(engine, this, 0.1);
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(task, 100, 10);
            repaint();
        }
        if(event.getSource()==butreset){
            timer.cancel();
            engine.reset();
            m=20;
            k=2;
            c=0.2;
            l=200;
            g=9.81;
            this.engine = new SimEngine(m, k, c, l, g, this.engine.getXm(), this.engine.getV(), new Vector2D(400, 30));
            this.task = new SimTask(engine, this, 0.1);
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(task, 100, 10);
            repaint();
        }

    }


    public void init(){
        this.engine = new SimEngine(m, k, c, l, g, new Vector2D(600, 400), new Vector2D(0, 0), new Vector2D(400, 30));
        this.task = new SimTask(engine, this, 0.1);
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(task, 100, 10);
        this.mousedrag=false;
        //nasłuchiwacze myszy
        addMouseListener(this);
        addMouseMotionListener(this);
        //elementy GUI
        setLayout(null);
        lm=new Label("Masa: ");
        lk=new Label("Wsp. sprężystości: ");
        lc=new Label("Wsp. tłumienia: ");
        ll=new Label("Dł. swobodna sprężyny: ");
        lg=new Label("Stała grawitacji: ");
        lm.setBounds(820,10,150,20);
        lk.setBounds(820,30,150,20);
        lc.setBounds(820,50,150,20);
        ll.setBounds(820,70,150,20);
        lg.setBounds(820,90,150,20);
        fm=new TextField();
        fk=new TextField();
        fc=new TextField();
        fg=new TextField();
        fl=new TextField();
        fm.setBounds(970,10,100,20);
        fk.setBounds(970,30,100,20);
        fc.setBounds(970,50,100,20);
        fl.setBounds(970,70,100,20);
        fg.setBounds(970,90,100,20);
        butreset=new Button("Resetuj");
        butzastosuj=new Button("Zastosuj");
        butzastosuj.setBounds(820,120,260,50);
        butreset.setBounds(820,190,260,50);
        butzastosuj.addActionListener(this);
        butreset.addActionListener(this);
        add(lm);
        add(lk);
        add(lc);
        add(ll);
        add(lg);
        add(fm);
        add(fk);
        add(fc);
        add(fl);
        add(fg);
        add(butzastosuj);
        add(butreset);
    }

    public void paint(Graphics g) {
        setSize(1100, 600);
        setBackground(Color.white);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, getWidth(), getHeight());
        g2.setPaint(Color.black);
        g2.draw(new Line2D.Double(800,1,800,600));
        Vector2D x = engine.getXm();
        Vector2D xz = engine.getXz();
        utwierdzenie(g2, xz);
        sprezyna(g2, xz, x);
        g2.setPaint(Color.black);
        g2.fillOval((int) x.getWspx() - 10, (int) x.getWspy() - 10, 20, 20);
        double x0=x.getWspx();
        double y0=x.getWspy();
        //rysowanie wektorow sil i predkosci
        g2.setColor(Color.red);
        engine.getV().rysowWekt(x0,y0,g2);
        g2.drawString("Wektor predkosci: "+engine.getV().info(1),10,(this.getHeight()-10));
        g2.setColor(Color.green);
        engine.getFg().rysowWekt(x0,y0,g2);
        g2.drawString("Wektor sily grawitacji: "+engine.getFg().info(1),10,(this.getHeight()-25));
        g2.setColor(Color.yellow);
        engine.getFw().rysowWekt(x0,y0,g2);
        g2.drawString("Wektor sily wiskotycznej: "+engine.getFw().info(1),10,(this.getHeight()-40));
        g2.setColor(Color.blue);
        engine.getFs().rysowWekt(x0,y0,g2);
        g2.drawString("Wektor sily sprezystosci: "+engine.getFs().info(1),10,(this.getHeight()-55));
        g2.setColor(Color.magenta);
        engine.getFwyp().rysowWekt(x0,y0,g2);
        g2.drawString("Wektor sily wypadkowej(w zaokragleniu): "+engine.getFw().info(1),10,(this.getHeight()-70));
    }
}