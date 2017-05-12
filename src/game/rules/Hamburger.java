package game.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hamburger {
    public static final Map<String, short[]> stringToRC = new HashMap<String, short[]>() {
        {
            put("a1", new short[] { 0, 0 });
            put("a4", new short[] { 1, 0 });
            put("a7", new short[] { 2, 0 });
            put("d7", new short[] { 3, 0 });
            put("g7", new short[] { 4, 0 });
            put("g4", new short[] { 5, 0 });
            put("g1", new short[] { 6, 0 });
            put("d1", new short[] { 7, 0 });

            put("b2", new short[] { 0, 1 });
            put("b4", new short[] { 1, 1 });
            put("b6", new short[] { 2, 1 });
            put("d6", new short[] { 3, 1 });
            put("f6", new short[] { 4, 1 });
            put("f4", new short[] { 5, 1 });
            put("f2", new short[] { 6, 1 });
            put("d2", new short[] { 7, 1 });

            put("c3", new short[] { 0, 2 });
            put("c4", new short[] { 1, 2 });
            put("c5", new short[] { 2, 2 });
            put("d5", new short[] { 3, 2 });
            put("e5", new short[] { 4, 2 });
            put("e4", new short[] { 5, 2 });
            put("e3", new short[] { 6, 2 });
            put("d3", new short[] { 7, 2 });
        }
    };

    //TODO
    public static final Map<String, List<short[][]>> mulan = new HashMap<String, List<short[][]>>() {
        {
            put("0,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,0},{1,0} });
                    add(new short[][] { {6,0},{7,0} });
                }
            });
            put("1,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,0},{0,0} });
                    add(new short[][] { {1,1},{1,2} });
                }
            });
            put("2,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,0},{1,0} });
                    add(new short[][] { {3,0},{4,0} });
                }
            });
            put("3,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,0},{4,0} });
                    add(new short[][] { {3,2},{3,1} });
                }
            });
            put("4,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,0},{3,0} });
                    add(new short[][] { {6,0},{5,0} });
                }
            });
            put("5,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,0},{6,0} });
                    add(new short[][] { {5,1},{5,2} });
                }
            });
            put("6,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,0},{5,0} });
                    add(new short[][] { {0,0},{7,0} });
                }
            });
            put("7,0", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,0},{6,0} });
                    add(new short[][] { {7,1},{7,2} });
                }
            });
            put("0,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {1,1},{2,1} });
                    add(new short[][] { {7,1},{6,1} });
                }
            });
            put("1,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,1},{2,1} });
                    add(new short[][] { {1,0},{1,2} });
                }
            });
            put("2,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {1,1},{0,1} });
                    add(new short[][] { {4,1},{3,1} });
                }
            });
            put("3,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,1},{2,1} });
                    add(new short[][] { {3,0},{3,2} });
                }
            });
            put("4,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,1},{3,1} });
                    add(new short[][] { {5,1},{6,1} });
                }
            });
            put("5,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,1},{6,1} });
                    add(new short[][] { {5,0},{5,2} });
                }
            });
            put("6,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,1},{5,1} });
                    add(new short[][] { {7,1},{0,1} });
                }
            });
            put("7,1", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,1},{6,1} });
                    add(new short[][] { {7,0},{7,2} });
                }
            });
            put("0,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {1,2},{2,2} });
                    add(new short[][] { {7,2},{6,2} });
                }
            });
            put("1,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,2},{2,2} });
                    add(new short[][] { {1,0},{1,1} });
                }
            });
            put("2,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,2},{1,2} });
                    add(new short[][] { {3,2},{4,2} });
                }
            });
            put("3,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,2},{4,2} });
                    add(new short[][] { {3,0},{3,1} });
                }
            });
            put("4,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {2,2},{3,2} });
                    add(new short[][] { {5,2},{6,2} });
                }
            });
            put("5,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,2},{6,2} });
                    add(new short[][] { {5,0},{5,1} });
                }
            });
            put("6,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {4,2},{5,2} });
                    add(new short[][] { {7,2},{6,2} });
                }
            });
            put("7,2", new ArrayList<short[][]>() {
                {
                    add(new short[][] { {0,2},{6,2} });
                    add(new short[][] { {7,0},{7,1} });
                }
            });
        }
    };

    //TODO
    public static final Map<String,List<short[]>> neighbors =  new HashMap<String, List<short[]>>() {
        {
            //angoli esterni
            put("0,0", new ArrayList<short[]>() {
                {
                    add(new short[] {1,0});
                    add(new short[] {7,0});
                }
            });
            put("2,0", new ArrayList<short[]>() {
                {
                    add(new short[] {1,0});
                    add(new short[] {3,0});
                }
            });
            put("4,0", new ArrayList<short[]>() {
                {
                    add(new short[] {3,0});
                    add(new short[] {5,0});
                }
            });
            put("6,0", new ArrayList<short[]>() {
                {
                    add(new short[] {5,0});
                    add(new short[] {7,0});
                }
            });
            //angoli centrale
            put("0,1", new ArrayList<short[]>() {
                {
                    add(new short[] {1,1});
                    add(new short[] {7,1});
                }
            });
            put("2,1", new ArrayList<short[]>() {
                {
                    add(new short[] {1,1});
                    add(new short[] {3,1});
                }
            });
            put("4,1", new ArrayList<short[]>() {
                {
                    add(new short[] {3,1});
                    add(new short[] {5,1});
                }
            });
            put("6,1", new ArrayList<short[]>() {
                {
                    add(new short[] {5,1});
                    add(new short[] {7,1});
                }
            });
            //angoli interno
            put("0,2", new ArrayList<short[]>() {
                {
                    add(new short[] {1,2});
                    add(new short[] {7,2});
                }
            });
            put("2,2", new ArrayList<short[]>() {
                {
                    add(new short[] {1,2});
                    add(new short[] {3,2});
                }
            });
            put("4,2", new ArrayList<short[]>() {
                {
                    add(new short[] {3,2});
                    add(new short[] {5,2});
                }
            });
            put("6,2", new ArrayList<short[]>() {
                {
                    add(new short[] {5,2});
                    add(new short[] {7,2});
                }
            });
            //centrali esterno
            put("1,0", new ArrayList<short[]>() {
                {
                    add(new short[] {0,0});
                    add(new short[] {2,0});
                    add(new short[] {1,1});
                }
            });
            put("3,0", new ArrayList<short[]>() {
                {
                    add(new short[] {2,0});
                    add(new short[] {4,0});
                    add(new short[] {3,1});
                }
            });
            put("5,0", new ArrayList<short[]>() {
                {
                    add(new short[] {4,0});
                    add(new short[] {6,0});
                    add(new short[] {5,1});
                }
            });
            put("7,0", new ArrayList<short[]>() {
                {
                    add(new short[] {6,0});
                    add(new short[] {0,0});
                    add(new short[] {7,1});
                }
            });
            //centrali interno
            put("1,2", new ArrayList<short[]>() {
                {
                    add(new short[] {0,2});
                    add(new short[] {2,2});
                    add(new short[] {1,1});
                }
            });
            put("3,2", new ArrayList<short[]>() {
                {
                    add(new short[] {2,2});
                    add(new short[] {4,2});
                    add(new short[] {3,1});
                }
            });
            put("5,2", new ArrayList<short[]>() {
                {
                    add(new short[] {4,2});
                    add(new short[] {6,2});
                    add(new short[] {5,1});
                }
            });
            put("7,2", new ArrayList<short[]>() {
                {
                    add(new short[] {6,2});
                    add(new short[] {0,2});
                    add(new short[] {7,1});
                }
            });
            //centrali centrale
            put("1,1", new ArrayList<short[]>() {
                {
                    add(new short[] {0,1});
                    add(new short[] {2,1});
                    add(new short[] {1,0});
                    add(new short[] {1,2});
                }
            });
            put("3,1", new ArrayList<short[]>() {
                {
                    add(new short[] {2,1});
                    add(new short[] {4,1});
                    add(new short[] {3,0});
                    add(new short[] {3,2});
                }
            });
            put("5,1", new ArrayList<short[]>() {
                {
                    add(new short[] {4,1});
                    add(new short[] {6,1});
                    add(new short[] {5,0});
                    add(new short[] {5,2});
                }
            });
            put("7,1", new ArrayList<short[]>() {
                {
                    add(new short[] {6,1});
                    add(new short[] {0,1});
                    add(new short[] {7,0});
                    add(new short[] {1,2});
                }
            });
        }
    };
}
