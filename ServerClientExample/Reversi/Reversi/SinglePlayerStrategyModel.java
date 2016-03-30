import java.util.ArrayList;

public class SinglePlayerStrategyModel extends SinglePlayerMainModel {

    final private static int INF = -9999999;
    final private static int board_size = get_board_size();
    final public static Point[] vectors = {new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0), new Point(1, 1), new Point(-1, -1), new Point(-1, 1), new Point(1, -1)};

    public static void test() {
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if (get_board(i, j) == -1) {
                    System.out.print(2);
                } else {
                    System.out.print(get_board(i, j));
                }
            }
            System.out.println("");
        }
    }

    public static int check_gain(int x, int y, int color) {
        int result = 0;
        if (get_board(x, y) != 0) {
            return result;
        }
        for (int k = 0; k < 8; k++) {
            int i = x, j = y, gain = 0;
            i += vectors[k].x;
            j += vectors[k].y;
            while (i >= 0 && j >= 0 && i < board_size && j < board_size && get_board(i, j) == -1 * color) {
                gain += 1;
                i += vectors[k].x;
                j += vectors[k].y;
            }
            if (i >= 0 && j >= 0 && i < board_size && j < board_size) {
                if (get_board(i, j) == color) {
                    result += gain;
                }
            }
        }
        return result;
    }

    public static boolean check_edge(int x, int y) {
        return x == 0 || y == 0 || x == board_size - 1 || y == board_size - 1;
    }

    public static boolean check_corner(int x, int y) {
        return x + y == 0 || (x == 0 && y == board_size - 1) || (x == board_size - 1 && y == 0) || x + y == board_size * 2 - 2;
    }

    public static boolean check_subcorner(int x, int y) {
        return (x == 1 && y == 1) || (x == 1 && y == board_size - 2) || (x == board_size - 2 && y == 1) || (x == board_size - 2 && y == board_size - 2);
    }

    public static boolean check_subedge(int x, int y) {
        return x == 1 || y == 1 || x == board_size - 2 || y == board_size - 2;
    }

    public static int get_score(int x, int y) {
        int color = get_board(x, y);
        if (color == 0) {
            return 0;
        }
        int score = 4;
        for (int k = 0; k < 4; k++) {
            int t1 = k * 2, t2 = t1 + 1;
            int s1 = 1, s2 = 1;
            int i = x, j = y;

            i += vectors[t1].x;
            j += vectors[t1].y;
            while (i >= 0 && j >= 0 && i < board_size && j < board_size && get_board(i, j) == color) {
                i += vectors[t1].x;
                j += vectors[t1].y;
            }
            if (i >= 0 && j >= 0 && i < board_size && j < board_size) {
                s1 = get_board(i, j) / color;
            }

            i = x;
            j = y;
            i += vectors[t2].x;
            j += vectors[t2].y;
            while (i >= 0 && j >= 0 && i < board_size && j < board_size && get_board(i, j) == color) {
                i += vectors[t2].x;
                j += vectors[t2].y;
            }
            if (i >= 0 && j >= 0 && i < board_size && j < board_size) {
                s2 = get_board(i, j) / color;
            }
            if (s1 + s2 == -1) {
                score -= 1;
            }
        }
        return score;
    }

    public static int evaluate() {
        int result = 0;
        for (int x = 0; x < board_size; x++) {
            for (int y = 0; y < board_size; y++) {
                if (check_corner(x, y)) {
                    result += get_board(x, y) * 64;
                } else if (check_edge(x, y)) {
                    result += get_board(x, y) * 8;
                } // else if (check_subcorner(x,y))
                // {
                // 	result -= get_board(x,y)*50;
                // }
                // else if (check_subedge(x,y))
                // {
                // 	result -= get_board(x,y)*5;
                // }
                else {
                    result += get_board(x, y) * get_score(x, y);
                }
            }
        }
        return result;
    }

    public static ArrayList<Point> get_all_possible(int color) {
        ArrayList<Point> result = new ArrayList<>();
        for (int i = 0; i < board_size; i++) {
            for (int j = 0; j < board_size; j++) {
                if (get_board(i, j) != 0) {
                    continue;
                }
                int temp = check_gain(i, j, color);
                if (temp > 0) {
                    Point tmp = new Point(i, j);
                    tmp.gain = temp;
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    public static Point search(int depth, int color) {
        Point result = new Point(-1, -1);
        result.gain = color * evaluate();
        if (depth == 0) {
            //result.gain = color*evaluate();
            return result;
        }
        //result.gain = INF;
        int _max = INF;
        ArrayList<Point> points = get_all_possible(color);
        for (Point point : points) {
            set_board(point.x, point.y, color);
            int val = -search(depth - 1, -1 * color).gain;
            if (val > _max) {
                result.gain = val;
                _max = val;
                result.x = point.x;
                result.y = point.y;
            }
            set_board(point.x, point.y, 0);
        }
        return result;
    }

    public static Point solution(int color) {
        Point result = search(4, color);
        result.color = color;
        return result;
    }
}
