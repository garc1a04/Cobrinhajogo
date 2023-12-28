import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	
	//Parte da tela
	static final int TELA_VERTICAL = 1280;
	static final int TELA_HORIZONTAL = 720;
	static final int SIZE = 50;
	static final int UNIDADES = (TELA_HORIZONTAL*TELA_VERTICAL)/(SIZE*SIZE);
	static int DELAY = 75;
	
	// posiçoes
	static int x[] = new int[UNIDADES];
	static int y[] = new int[UNIDADES];
	
	//Corpo e maca
	int Corpo = 6;
	int macasComidas;
	int macaX;
	int macaY;
	
	//Sentidos e direcoes
	char direcoes = 'B';
	boolean correr = false;
	boolean ganhou;
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		setPreferredSize(new Dimension(TELA_VERTICAL,TELA_HORIZONTAL));
		setBackground(new Color(27,65,25));
		setFocusable(true);
		addKeyListener(new minhaKeys());
		Comeco();
	}
	public void Comeco(){
		novaMaca();
		correr = true;
		timer = new Timer(DELAY, this);
		timer.start();
			
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		desenho(g);
	}
	public void ganhou(Graphics g) {
		correr = false;
		ganhou = true;
		setBackground(new Color(1,1,1));
		FontMetrics font = getFontMetrics(g.getFont());
		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial",Font.ITALIC,25));
		g.drawString("PARABENS VOCÊ GANHOU COM "+macasComidas+" MAÇAS PEGAS", 
				(TELA_VERTICAL-font.stringWidth("PARABENS VOCÊ GANHOU COM "+macasComidas+" MAÇAS PEGAS"))/3, 
				TELA_HORIZONTAL/2);
	}
	public void desenho(Graphics g) {
		if(correr) {
			if(macasComidas == 2) {
				ganhou(g);
			}
			else {
				//Maça
				g.setColor(Color.red);
				g.fillOval(macaX,macaY,SIZE,SIZE);
				
				//Definições do corpo
				for(int i =0;i<Corpo;i++) {
					if(x[0]==x[i]) {
						g.setColor(Color.green);
						g.fillOval(x[i], y[i],SIZE,SIZE);
					}else {
						g.setColor(Color.GREEN);
						g.fillOval(x[i], y[i],SIZE,SIZE);
					}
				}
				//Fonte para Pontuação
				g.setColor(Color.red);
				g.setFont(new Font("Arial",Font.ITALIC,25));
				FontMetrics font = getFontMetrics(g.getFont());
				g.drawString("Pontuaçao:" + macasComidas, 
						(TELA_VERTICAL-font.stringWidth("Pontuaçao: " + macasComidas))/2, 
						g.getFont().getSize());				
			}
			
		}if(!correr && !ganhou) {
			perdeu(g);
		}if(!correr && ganhou) {
			ganhou(g);
		}
		
	}
	public void mover() {
			for(int i = Corpo; i > 0 ;i--) {
					x[i] = x[i-1];
					y[i] = y[i-1];
		}
			switch(direcoes) {
				case 'D': 
					x[0] = x[0]+SIZE;
					break;
				case 'E': 
					x[0] = x[0]-SIZE;
					break;
				case 'B': 
					y[0] = y[0]+SIZE;
					break;
				case 'C': 
					y[0] = y[0]-SIZE;
					break;
			}

				
	}
	public void novaMaca() {
		macaX = random.nextInt((TELA_VERTICAL/SIZE))*SIZE;
		macaY = random.nextInt((TELA_HORIZONTAL/SIZE))*SIZE;	
	}
	public void checarMacas(){
		if(x[0] == macaX && y[0] == macaY) {
				Corpo+=1;
				macasComidas++;
				novaMaca();					
		}
	}
	public void checarColisoes(){
		
		//Colisoes do corpo
		for(int i = Corpo;i > 0 ;i--) {
			if(x[0] == x[i] && y[0] == y[i]) {
				correr = false;
			}
		}
		//Checar colisoes da parede
		if(x[0] < 0) {
			correr = false;
		}
		if(x[0] > TELA_VERTICAL) {
			correr = false;
		}
		if(y[0] < 0) {
			correr = false;
		}
		if(y[0] > TELA_HORIZONTAL) {
			correr = false;
		}
		if(!correr) {
			timer.stop();
		}
	}
	public void perdeu(Graphics g) {
		setBackground(Color.black);
		g.setColor(Color.red);
		g.setFont(new Font("Arial",Font.ITALIC,40));
		FontMetrics font = getFontMetrics(g.getFont());
		g.drawString("Perdeu irmão :(", 
				(TELA_VERTICAL-font.stringWidth("Perdeu irmão :("))/2, TELA_HORIZONTAL/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(correr) {
			mover();
			checarMacas();
			checarColisoes();
		}
		
		repaint();
		
	}
	
	public class minhaKeys extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if(direcoes !='B') {
					direcoes = 'C';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direcoes !='C') {
					direcoes = 'B';
				}
				break;
			case KeyEvent.VK_LEFT:
				if(direcoes !='D') {
					direcoes = 'E';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direcoes !='E') {
					direcoes = 'D';
				}
				break;
			}
		}
		
	}
	
}
