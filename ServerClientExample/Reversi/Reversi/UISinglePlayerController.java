import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;

class UISinglePlayerController extends UIInterface {

    private final UISinglePlayerView viewPanel;
    private final JLabel[][] btn;
   
    private static int hintNum;      // Current num of available hints
    private static int undoNum;      // Current num of available undos
    private int bNum, wNum;   // Total num of black, white pieces on board
    private boolean inGame;
    private boolean canUndo;
    private boolean can_response = false;
    
    private final int tileLength;
    private final int gifFrames;
    private final int maxUndos = UISettings.getHint();
    private final int maxHints = UISettings.getUndo();
    
    public UISinglePlayerController() throws IOException {
        
        this.setLayout(new BorderLayout());
        viewPanel = new UISinglePlayerView();
        
        // initialize variables
        tileLength = viewPanel.tileLength;
        gifFrames = viewPanel.gifFrames;
        hintNum = maxHints;
        undoNum = maxUndos;
        bNum = 0;
        wNum = 0;
        inGame = false;
        canUndo = false;

        SinglePlayerMainModel.load_music();
        
        btn = new JLabel[tileLength][tileLength];
        initComponents();   // Creates mouseListeners
        
        this.add(viewPanel);
        this.setVisible(true);
    }
    
    public void set_last(int i, int j, int color){
        switch (color){
            case 1: btn[i][j].setIcon(viewPanel.white2); break;
            case -1: btn[i][j].setIcon(viewPanel.black2); break;
            default: btn[i][j].setIcon(null);break;
        }
    }
    
    public void set_btn(){
        for (int i = 0; i < tileLength;i++){
            for (int j = 0;j < tileLength;j++){
                //btn[i][j].setVisible(true);
                switch (SinglePlayerMainModel.get_board(i,j)){
                    case 1: btn[i][j].setIcon(viewPanel.white); break;
                    case -1: btn[i][j].setIcon(viewPanel.black); break;
                    default: btn[i][j].setIcon(null);break;
                }
            }
        }
    }
    
    public static int get_hint_num(){return hintNum;}
    public static int get_undo_num(){return undoNum;}
    public static void set_hint_num(int num){hintNum = num;}
    public static void set_undo_num(int num){undoNum = num;}

    private void set_num() {
        bNum = wNum = 0;
        for (int i = 0; i < tileLength; i++) {
            for (int j = 0; j < tileLength; j++) {
                if (SinglePlayerMainModel.get_board(i, j) == -1) {
                    bNum += 1;
                }
                if (SinglePlayerMainModel.get_board(i, j) == 1) {
                    wNum += 1;
                }
            }
        }
        viewPanel.setScore(String.valueOf(wNum), 1);
        viewPanel.setScore(String.valueOf(bNum), -1);
    }
    
    private void refresh() {
        set_btn();
        set_num();
        viewPanel.repaint();
    }

    public void change_color(ArrayList<Integer> i, ArrayList<Integer> j, int color) throws InterruptedException {
        if (i.size() != j.size()) {
            return;
        }
        int frameWait = 30;
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    if (color == -1) {
                        for (int n = 0; n < gifFrames; ++n) {
                            for (int _i = 0; _i < i.size(); ++_i) {
                                btn[i.get(_i)][j.get(_i)].setIcon(viewPanel.wtob[n]);
                            }
                            this.sleep(frameWait);
                        }
                    } else {
                        for (int n = 0; n < gifFrames; ++n) {
                            for (int _i = 0; _i < i.size(); ++_i) {
                                btn[i.get(_i)][j.get(_i)].setIcon(viewPanel.btow[n]);
                            }
                            this.sleep(frameWait);
                        }
                    }
                } catch (InterruptedException ex) {
                }
            }
        };
        th.start();
    }

    public void hint() {
        SinglePlayerMainModel.Point step = SinglePlayerStrategyModel.solution(SinglePlayerMainModel.player_color);
        if (step.x == -1) {
            return;
        }
        btn[step.x][step.y].setIcon(viewPanel.hint);
        hintNum -= 1;
    }

    public void response() {
        SinglePlayerMainModel.Point next_step = SinglePlayerStrategyModel.solution(-1 * SinglePlayerMainModel.player_color);
        if (next_step.x == -1 || next_step.y == -1) // if computer cannot move..
        {
            refresh();
            message(GameControlCommand.COMPUTER_STUCK);
        } else {
            SinglePlayerMainModel.make_step(next_step);
            refresh();
            try {
                change_color(SinglePlayerMainModel.get_x(), SinglePlayerMainModel.get_y(), -1 * SinglePlayerMainModel.player_color);
            } catch (InterruptedException ex) {}

            set_last(next_step.x, next_step.y, -1 * SinglePlayerMainModel.player_color);

            //computer consecutive moves
            if (SinglePlayerStrategyModel.solution(SinglePlayerMainModel.player_color).x != -1) {
                can_response = false;
            } else {
                message(GameControlCommand.USER_STUCK);
            }
        }
        check_end();
    }

    public void check_end() {
        boolean all_stuck = false;
        if (SinglePlayerStrategyModel.solution(SinglePlayerMainModel.player_color).x == -1 && SinglePlayerStrategyModel.solution(-1 * SinglePlayerMainModel.player_color).x == -1) {
            all_stuck = true;
        }
        if (all_stuck || bNum + wNum == tileLength * tileLength || inGame && (bNum == 0 || wNum == 0)) {
            if (bNum > wNum) {
                message(GameControlCommand.WIN);
            } else if (bNum == wNum) {
                message(GameControlCommand.DRAW);
            } else {
                message(GameControlCommand.LOSE);
            }
            undoNum = 0;
            hintNum = 0;
            inGame = false;
            canUndo = false;
            can_response = false;
        }
    }

    public void make_step(int i, int j) {
        if (!inGame) { return; }
        if (SinglePlayerMainModel.make_step(new SinglePlayerMainModel.Point(i, j, SinglePlayerMainModel.player_color))) {
            refresh();
            try {
                change_color(SinglePlayerMainModel.get_x(), SinglePlayerMainModel.get_y(), SinglePlayerMainModel.player_color);
            } catch (InterruptedException ie) {
            }
            set_last(i, j, SinglePlayerMainModel.player_color);

            message(GameControlCommand.EXIT);
            canUndo = true;
            can_response = true;
            check_end();
        } else {
            message(GameControlCommand.INVALID);
            Timer timer = new Timer(2000, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Runnable doRun = () -> {
                        message(GameControlCommand.EXIT);
                    };
                    SwingUtilities.invokeLater(doRun);
                }
            });
            timer.start();
            timer.setRepeats(false);
        }
    }

    private void initComponents() {  // Initialize components and set actions
        // Initialize game board
        for (int i = 0; i != tileLength; i++) {
            for (int j = 0; j != tileLength; j++) {
                btn[i][j] = new JLabel();
                viewPanel.p_board.add(btn[i][j]);
                btn[i][j].setOpaque(false);
                final int _i = i, _j = j;
                btn[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) { 
                        //System.out.println(_i + " " +_j);
                        make_step(_i, _j); 
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (can_response) {
                            response();
                        }
                    }
                });
            }
        }

        // button Mouselisteners
        for (int i = 0; i < viewPanel.iconNum; ++i){
            final int _i = i;
            viewPanel.label[i].addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    switch (_i){
                        case UISinglePlayerView.UNDO:
                            if (undoNum > 0 && inGame && canUndo) {
                                undoNum -= 1;SinglePlayerMainModel.back_step();
                                refresh(); canUndo= false;
                            } break;
                        case UISinglePlayerView.HINT: if (hintNum > 0) {hint();} break;
                        case UISinglePlayerView.MUSIC: SinglePlayerMainModel.change_music(); break;
                        case UISinglePlayerView.NEW: SinglePlayerMainModel.init(); inGame = true;
                            hintNum = maxHints; undoNum = maxUndos; canUndo = false;
                            refresh(); break;
                        case UISinglePlayerView.SAVE: if (inGame) {SinglePlayerMainModel.save(); message(GameControlCommand.SAVE);} else message(GameControlCommand.UNSAVEABLE); refresh(); break;
                        case UISinglePlayerView.LOAD: SinglePlayerMainModel.load(); message(GameControlCommand.LOADED); canUndo = false; inGame = true; refresh(); break;
                        case UISinglePlayerView.HOME: performSegue(UIViewEnum.MENU);
                        default: break;
                    }
                }
                @Override
                public void mousePressed(MouseEvent e) { viewPanel.setPressed(_i);}
                @Override
                public void mouseReleased(MouseEvent e) { viewPanel.setReleased(_i);}
                @Override
                public void mouseEntered(MouseEvent e) {
                    switch (_i){
                        case 0: message(GameControlCommand.UNDO); break;
                        case 1: message(GameControlCommand.HINT); break;
                        case 5: message(GameControlCommand.MUSIC); break;
                        case 2: message(GameControlCommand.NEW); break;
                        case 3: message(GameControlCommand.SAVE); break;
                        case 4: message(GameControlCommand.LOAD); break;
                        case 6: message(GameControlCommand.HOME); break;
                        default: break;
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {message(GameControlCommand.EXIT);}
            });}
    }

    private void message(GameControlCommand msg) {  // Mousing over the label-buttons makes a message show up in the messgeboard
        switch (msg){
            case UNDO:
                if (undoNum > 0 && canUndo) {
                    viewPanel.changeMsg("Undo last move ("+Integer.toString(undoNum)+" chances left)");
                } else {
                    viewPanel.changeMsg("You can't undo anymore!");
                }
            break;
            case HINT:
                if (hintNum > 0) {
                    viewPanel.changeMsg("Get a hint (" +  hintNum + " hints left)");
                } else {
                    viewPanel.changeMsg("You don't have any hints left!");
                }
                break;
            case MUSIC:
                viewPanel.changeMsg("Next song");
                break;
            case NEW:  viewPanel.changeMsg("Start a new game"); break;
            case SAVE: viewPanel.changeMsg("Save game"); break;
            case LOAD: viewPanel.changeMsg("Load saved game"); break;
            case HOME: viewPanel.changeMsg("Back to menu"); break;
            case INVALID: viewPanel.changeMsg("Invalid move!"); break;
            case SAVED: viewPanel.changeMsg("Game saved!"); break;
            case UNSAVEABLE: viewPanel.changeMsg("You haven't started playing yet!"); break;
            case LOADED: viewPanel.changeMsg("Loaded saved game"); break;
            case WIN: viewPanel.changeMsg("You Win!"); break;
            case LOSE: viewPanel.changeMsg("You Lose!"); break;
            case DRAW: viewPanel.changeMsg("It's a draw"); break;
            case COMPUTER_STUCK: viewPanel.changeMsg("Make another move!"); break;
            case USER_STUCK: viewPanel.changeMsg("You can't move now!"); break;
            default:
                if ("".equals(SinglePlayerMainModel.music)) break;
                String prefix = "music/";
                String name = SinglePlayerMainModel.music.substring(prefix.length());
                viewPanel.changeMsg("Now playing: "+ name);
                break;
        }
    }
    
}
