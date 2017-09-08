package game;

import game.sprites.Laser;
import game.sprites.Sprite;
import game.sprites.Enemy;
import game.sprites.Marca;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.Lib;

public final class Juego
extends Canvas {
    private final ArrayList<Sprite> spritesStaticos = new ArrayList();
    private final ArrayList<Sprite> spritesDinamicos = new ArrayList();
    private final ArrayList<Enemy> enemies = new ArrayList();
    private final ArrayList<Sprite> proyectilesPropios = new ArrayList();
    private final ArrayList<Sprite> proyectilesEnemigos = new ArrayList();
    private Frame ventana;
    private final Sprite planeta = new Sprite();
    private final Sprite fondo = new Sprite();
    private final Enemy myShip = new Enemy();
    private Marca marca;
    BufferedImage pantalla;
    int refreshTime = 25;
    long tiempo = System.currentTimeMillis();
    JPanel controles;
    private int dificult = 1;
    private boolean clear = false;

    public Juego(){
        //cheats
        myShip.setMaxArmor(5000d);
        myShip.setDamageAmplifier(25);
        //configurar y setear cantidad enemigos
        configure();       
        runGame();
    }    
    
    public void dibuja(Graphics grafico) {
        int i;
        this.pantalla = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        int cantidadEstaticos = this.spritesStaticos.size();
        this.spritesStaticos.get(0).setX(0.0);
        this.spritesStaticos.get(0).setY(0.0);
        for (int i2 = 0; i2 < cantidadEstaticos; ++i2) {
            if (this.spritesStaticos.get(i2).mustBeDestroy()) {
                this.spritesStaticos.remove(i2);
                continue;
            }
            Double X = this.spritesStaticos.get(i2).getX();
            Double Y = this.spritesStaticos.get(i2).getY();
            this.spritesStaticos.get(i2).putSprite(this.pantalla.getGraphics(), X, Y);
        }
        int cantidadDinamicos = this.spritesDinamicos.size();
        for (int i3 = 0; i3 < cantidadDinamicos; ++i3) {
            if (this.spritesDinamicos.get(i3).mustBeDestroy()) {
                this.spritesDinamicos.remove(i3);
                break;
            }
            this.spritesDinamicos.get(i3).move();
            this.spritesDinamicos.get(i3).putSprite(this.pantalla.getGraphics());
        }
        int cantidadProyectiles = this.proyectilesPropios.size();
        for (i = 0; i < cantidadProyectiles; ++i) {
            if (this.proyectilesPropios.get(i).mustBeDestroy()) {
                this.proyectilesPropios.remove(i);
                break;
            }
            this.proyectilesPropios.get(i).move();
            this.proyectilesPropios.get(i).putSprite(this.pantalla.getGraphics());
        }
        cantidadProyectiles = this.proyectilesEnemigos.size();
        for (i = 0; i < cantidadProyectiles; ++i) {
            if (this.proyectilesEnemigos.get(i).mustBeDestroy()) {
                this.proyectilesEnemigos.remove(i);
                break;
            }
            this.proyectilesEnemigos.get(i).move();
            this.proyectilesEnemigos.get(i).putSprite(this.pantalla.getGraphics());
        }
        int cantidadEnemys = this.enemies.size();
        for (int i4 = 0; i4 < cantidadEnemys; ++i4) {
            if (((Sprite)this.enemies.get(i4)).mustBeDestroy()) {
                this.enemies.remove(i4);
                break;
            }
            ((Sprite)this.enemies.get(i4)).move();
            ((Sprite)this.enemies.get(i4)).putSprite(this.pantalla.getGraphics());
        }
        grafico.drawImage(this.pantalla, 0, 0, this);
    }
    
    public void configure(){
        
        this.marca = new Marca();
        this.ventana = new JFrame();
        this.ventana.setLayout(new BorderLayout());
        this.controles = new JPanel();
        this.controles.add(new JButton("OK"));
        this.ventana.add((Component)this.controles, "South");
        this.ventana.setSize(1024, 500);
        this.ventana.setExtendedState(6);
        this.ventana.setIconImage(new ImageIcon(this.getClass().getResource("/Imagenes/Marca/marca2b.png")).getImage());
        this.ventana.add(this);
        this.ventana.setVisible(true);
        this.ventana.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
         
        this.planeta.setSprite("/Imagenes/planeta-agua.png");
        this.fondo.setSprite("/Imagenes/space.jpg");
        this.planeta.setX(80.0);
        this.planeta.setY(80.0);
        this.spritesStaticos.add(this.fondo);
        this.spritesStaticos.add(this.planeta);
        this.spritesDinamicos.add(this.marca);
        this.spritesDinamicos.add(this.myShip);
        
        this.addMouseListener(new MouseEvent());        
        setClear();
    }
    
    
    public void setClear(){       
        //set enemies
        enemies.clear();
        for (int i = 0; i<dificult; i++){
            this.enemies.add((Enemy) new Enemy().setX(Lib.getRandomWidth(this)).setY(Lib.getRandomHeight(this)).setSprite("/Imagenes/Nave/splitter.png"));
        }
    }
    
    public void runGame(){
        
        do {            
            if (System.currentTimeMillis() - this.tiempo <= (long)this.refreshTime) {
                continue;
            }
            if (this.marca.isVisible() && this.marca.intecerpta(this.myShip)) {
                this.marca.setVisible(false);
            }
            int maxProyectiles = this.proyectilesPropios.size();
            int maxEnemies = this.enemies.size();
            for (int i = 0; i < maxProyectiles; ++i) {
                for (int j = 0; j < maxEnemies; ++j) {
                    if (!this.enemies.get(j).intecerpta(this.proyectilesPropios.get(i))) continue;
                    this.enemies.get(j).receiveDamage(((Laser)this.proyectilesPropios.get(i)).hit());
                }
            }
            if (maxEnemies == 0) {
                dificult++;
                clear = true;
                setClear();
                //JOptionPane.showMessageDialog(this.ventana, "You Win", "Alerta", 1);
                //System.exit(0);
            }
            maxProyectiles = this.proyectilesEnemigos.size();
            for (int i = 0; i < maxProyectiles; ++i) {
                if (!this.myShip.intecerpta(this.proyectilesEnemigos.get(i))) continue;
                this.myShip.receiveDamage(((Laser)this.proyectilesEnemigos.get(i)).hit());
            }
            maxEnemies = this.enemies.size();
            for (int i = 0; i < maxEnemies; ++i) {
                if (this.myShip.intecerpta(this.enemies.get(i)) && !this.myShip.mustBeDestroy()) {
                    this.myShip.receiveDamage(1.0);
                    this.enemies.get(i).receiveDamage(1.0);
                }
                if ((this.tiempo % 103 == 0 || this.enemies.get((int)i).getRestanteX() == 0.0 && this.enemies.get((int)i).getRestanteY() == 0.0) && !this.enemies.get((int)i).isDestroing()) {
                    this.enemies.get(i).moveTo(Lib.getRandomWidth(this), Lib.getRandomHeight(this));                    
                    this.proyectilesEnemigos.add(new Laser(enemies.get(i),myShip));
                }
                if (this.tiempo % (long)((int)(Math.random() * 80.0) + 1) != 0 || this.enemies.get((int)i).isDestroing()) continue;
                
                this.proyectilesEnemigos.add(new Laser(enemies.get(i),myShip));
            }
            if (!this.myShip.mustBeDestroy()) {
                this.dibuja(this.getGraphics());
            } else {
                JOptionPane.showMessageDialog(this.ventana, "You Lose", "Alerta", 1);
                System.exit(0);
               
            }
            this.tiempo = System.currentTimeMillis();
        } while (!clear);
        clear = false;
        System.out.println(myShip.getArmor());
        runGame();
    }
    
    public static void main(String[] arg) {
        Juego game = new Juego();
    }

    private class MouseEvent
    implements MouseListener {
        private MouseEvent() {
        }

        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (!Juego.this.myShip.isIn(new Double(e.getX()), new Double(e.getY())) || e.getModifiers() == 16 || e.getModifiers() == 4 || e.getModifiers() == 8) {
                // empty if block
            }
        }

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            //if (!Juego.access$100((Juego)Juego.this).isDestroing) {
            switch (e.getModifiers()) {
                case 16:
                    Juego.this.myShip.rotar(new Double(e.getX()), new Double(e.getY()));
                    Laser laser = new Laser(Juego.this.myShip, e.getX(), e.getY());
                    Juego.this.proyectilesPropios.add(laser);
                    break;
            // }
                case 4:
                    int x = e.getX();
                    int y = e.getY();
                    int minimoX = Juego.this.myShip.getWidth() / 2;
                    int maximoX = Juego.this.pantalla.getWidth() - Juego.this.myShip.getWidth() / 2;
                    int minimoY = Juego.this.myShip.getHeight() / 2;
                    int maximoY = Juego.this.pantalla.getHeight() - Juego.this.myShip.getHeight() / 2 - Juego.this.controles.getHeight() / 2;
                    if (x < minimoX) {
                        x = minimoX;
                    } else if (x > maximoX) {
                        x = maximoX;
                    }
                    if (y < minimoY) {
                        y = minimoY;
                    } else if (y > maximoY) {
                        y = maximoY;
                    }
                    Juego.this.myShip.moveTo(new Double(x), new Double(y));
                    Juego.this.marca.setVisible(true);
                    Juego.this.marca.moveTo(new Double(x), new Double(y));
                    break;            
                case 8:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
        }
    }

}

