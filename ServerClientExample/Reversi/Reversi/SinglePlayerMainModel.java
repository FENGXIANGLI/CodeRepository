import java.util.ArrayList;
import java.io.*;

public class SinglePlayerMainModel {

    private static ArrayList<Integer> changed_x = new ArrayList<>();
    private static ArrayList<Integer> changed_y = new ArrayList<>();

    public static int player_color = -1;
    private static int next_color;
    private static final int board_size = 8;
    private static Point last_step;
    private static int[][] board_cache = new int[board_size][board_size];
    final private static String filename = "gamefile.txt";
    public static String music = "";
    private static MusicPlayer p;
    private static ArrayList<String> music_list = new ArrayList<>();
    private static int[][] board = new int[board_size][board_size];

    public static class Point {

        int x;
        int y;
        int color;
        int gain;

        public Point(int _x, int _y) {
            this.x = _x;
            this.y = _y;
        }

        public Point(int _x, int _y, int _color) {
            this.x = _x;
            this.y = _y;
            this.color = _color;
        }
    }

    public static int get_board(int i, int j) {
        return board[i][j];
    }

    public static void set_board(int i, int j, int color) {
        board[i][j] = color;
    }

    public static int get_board_size() {
        return board_size;
    }

    public static enum STATUS {

        NORMAL, CONTINUE, WIN, LOSE, ERROR;
    }

    public static STATUS get_status() {
        return STATUS.NORMAL;
    }

    public static void load_music() throws IOException {
        InputStream input = SinglePlayerMainModel.class.getResourceAsStream("music/list.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String song;
        while (true) {
            song = reader.readLine();
            if (song == null) {
                break;
            }
            music_list.add("music/" + song);
        }
		// File music_dir = new File("music");
        // File filelist[] = music_dir.listFiles();
        // for (int i = 0;i < filelist.length;i++)
        // {
        // 	if (filelist[i].isFile() && !filelist[i].getName().equals(".DS_Store"))
        // 	{
        // 		music_list.add("music/"+filelist[i].getName());
        // 	}
        // }
    }

    public static void play_music() {
        if (p != null) {
            p.stop();
        }
        p = new MusicPlayer(music);
        p.start();
    }

    public static void change_music() {
        String newMusic = "";
        for (int i = 0; i < music_list.size(); i++) {
            newMusic = music_list.get(i);
            if (newMusic == null ? music == null : newMusic.equals(music)) {
                if (i == music_list.size() - 1) {
                    newMusic = music_list.get(0);
                } else {
                    newMusic = music_list.get(i + 1);
                }
                break;
            }
        }
        music = newMusic;
		//System.out.println(music);

        play_music();
    }

    public static void init() {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if ((i == board_size / 2 && j == board_size / 2 - 1) || (i == board_size / 2 - 1 && j == board_size / 2)) {
                    board[i][j] = -1;
                } else if ((i == board_size / 2 && j == board_size / 2) || (i == board_size / 2 - 1 && j == board_size / 2 - 1)) {
                    board[i][j] = 1;
                } else {
                    board[i][j] = 0;
                }
            }
        }

        if (p != null) {
            return;
        }
        change_music();
    }

    public static boolean make_step(Point step) {
        changed_x.clear();
        changed_y.clear();
        if (SinglePlayerStrategyModel.check_gain(step.x, step.y, step.color) == 0) {
            return false;
        }
        if (step.color == player_color) {
            for (int i = 0; i < board_size; i++) {
                System.arraycopy(board[i], 0, board_cache[i], 0, board_size);
            }
        }
        board[step.x][step.y] = step.color;
        next_color = -1 * step.color;
        for (int k = 0; k < board_size; k++) {
            int i = step.x, j = step.y;
            i += SinglePlayerStrategyModel.vectors[k].x;
            j += SinglePlayerStrategyModel.vectors[k].y;
            while (i >= 0 && j >= 0 && i < board_size && j < board_size && board[i][j] == -1 * step.color) {
                i += SinglePlayerStrategyModel.vectors[k].x;
                j += SinglePlayerStrategyModel.vectors[k].y;
            }
            if (i >= 0 && j >= 0 && i < board_size && j < board_size && board[i][j] == step.color) {
                while (i != step.x || j != step.y) {
                    i -= SinglePlayerStrategyModel.vectors[k].x;
                    j -= SinglePlayerStrategyModel.vectors[k].y;
                    board[i][j] = step.color;

                    if (i != step.x || j != step.y) {
                        changed_x.add(i);
                        changed_y.add(j);
                    }
                }
            }
        }
        return true;
    }

    public static void back_step() {
        for (int i = 0; i < board_size; i++) {
            System.arraycopy(board_cache[i], 0, board[i], 0, board_size);
        }
    }

    public static boolean save() {
        try {
            File game = new File(filename);
            if (!game.exists()) {
                game.createNewFile();
            }
            FileWriter fw = new FileWriter(filename);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                for (int i = 0; i < board_size; i++) {
                    String temp = "";
                    for (int j = 0; j < board_size; j++) {
                        if (board[i][j] == -1) {
                            temp += Integer.toString(2);
                        } else {
                            temp += Integer.toString(board[i][j]);
                        }
                    }
                    temp += "\n";
                    bw.write(temp);
                }
                bw.write(Integer.toString(UISinglePlayerController.get_hint_num()));
                bw.write(Integer.toString(UISinglePlayerController.get_undo_num()));
            }
        } catch (IOException ie) {
            return false;
        }
        return true;
    }

    public static boolean load() {
        try {
            File game = new File(filename);
            if (!game.exists()) {
                return false;
            }
            FileReader fr = new FileReader(game);
            try (BufferedReader br = new BufferedReader(fr)) {
                for (int i = 0; i < board_size; i++) {
                    String line;
                    line = br.readLine();
                    for (int j = 0; j < board_size; j++) {
                        board[i][j] = Integer.parseInt(line.charAt(j) + "");
                        if (board[i][j] == 2) {
                            board[i][j] = -1;
                        }
                    }
                }
                String num = br.readLine();
                UISinglePlayerController.set_hint_num(Integer.parseInt(num.charAt(0) + ""));
                UISinglePlayerController.set_undo_num(Integer.parseInt(num.charAt(1) + ""));
            }
        } catch (IOException ie) {
            return false;
        }
        return true;
    }

    public static ArrayList<Integer> get_x() {
        return changed_x;
    }

    public static ArrayList<Integer> get_y() {
        return changed_y;
    }
}
