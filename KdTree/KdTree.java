import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private int n;
    private Node root;

    public KdTree() {
        n = 0;
        root = null;
    }

    /* Khoi tao cac node cua cay */
    private class Node {
        private final Point2D point;
        private final RectHV rect;
        private Node left;
        private Node right;

        public Node(Point2D p, RectHV rect) {
            point = p;
            this.rect = rect;
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else {
            root = insertNode(root, p, 1, 0.0, 0.0, 1.0, 1.0);
        }
    }

    /* Hàm đệ quy để tìm vị trí cần insert
        direction % 2 == 0 : vertical
        direction % 2 != 0 : horizontal
        Nếu node đó là vertical thì ta so sánh X của điểm p và X của điểm tại node đó.
        Nếu node đó là horizontal thì ta so sánh Y của điểm p và Y của điểm tại node đó.
        Ta sử dụng đệ quy để có thể chạy đan xen nhau giữa các node ngang và dọc
     */
    private Node insertNode(Node node, Point2D p, int direction, double x0, double y0, double x1, double y1) {
        if (node == null) {
            n++;
            return new Node(p, new RectHV(x0, y0, x1, y1));
        }
        if (direction % 2 == 0) {
            if (p.x() < node.point.x()) {
                node.left = insertNode(node.left, p, direction + 1, x0, y0, node.point.x(), y1);
            } else {
                node.right = insertNode(node.right, p, direction + 1, node.point.x(), y0, x1, y1);
            }
        } else {
            if (p.y() < node.point.y()) {
                node.left = insertNode(node.left, p, direction + 1, x0, y0, x1, node.point.y());
            } else {
                node.right = insertNode(node.right, p, direction + 1, x0, node.point.y(), x1, y1);
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return containNode(root, p, 1);
    }

    /* Tương tự như insert Node, ta cũng sử dụng đệ quy để đan xen giữa các node dọc và ngang.
        Nếu node cần tìm bé hơn node đang trỏ đến thì dịch con trỏ sang nhánh bên trái, ngược lại là nhánh phải.
        Tại các node ngang và dọc thì việc so sánh giữa các node giống với insert node
     */
    private boolean containNode(Node node, Point2D p, int direction) {
        if (node == null) {
            return false;
        } else if (p.x() == node.point.x() && p.y() == node.point.y()) {
            return true;
        } else {
            if (direction % 2 == 0) {
                if (p.x() < node.point.x()) {
                    return containNode(node.left, p, direction + 1);
                } else {
                    return containNode(node.right, p, direction + 1);
                }
            } else {
                if (p.y() < node.point.y()) {
                    return containNode(node.left, p, direction + 1);
                } else {
                    return containNode(node.right, p, direction + 1);
                }
            }
        }
    }

    // Vẽ hình
    public void draw() {
        drawNode(root, 1);
    }

    private void drawNode(Node node, int direction) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        if (direction % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(), node.point.y());
        }
        drawNode(node.left, direction + 1);
        drawNode(node.right, direction + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> st = new Stack<>();
        addNodeToStack(root, rect, st);
        return st;
    }

    // Tìm kiếm các node nằm trong hình chữ nhật dc cho. Chạy đệ quy để kiểm tra cả 2 nhánh trái và phải.
    private void addNodeToStack(Node node, RectHV rect, Stack<Point2D> st) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.point)) {
            st.push(node.point);
        }
        if (rect.intersects(node.rect)) {
            addNodeToStack(node.left, rect, st);
            addNodeToStack(node.right, rect, st);
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return nearestPoint(root, p, root.point, 1);
    }

    /* kiểm tra khoảng cách giữa các node
        nếu node đang trỏ tới có khoảng cách nhỏ hơn khoảng cách của node nhỏ nhất thì ta gán node nhỏ nhất bằng node đang trỏ tới
        vì mỗi node đều chia hình ra làm 2 hình chữ nhật nên để tối ưu hóa cách làm, ta xem rằng hình chữ nhật nào gần node P hơn và sau đó luôn duyệt subtree
        nằm trên hình chữ nhật đó đầu tiên để tìm điểm có khoảng cách gần nhất rồi mới duyệt nửa còn lại.
     */
    private Point2D nearestPoint(Node node, Point2D p, Point2D closest, int direction) {
        Point2D res = closest;
        if (node == null) {
            return res;
        }
        if (node.point.distanceSquaredTo(p) < res.distanceSquaredTo(p)) {
            res = node.point;
        }
        if (node.rect.distanceSquaredTo(p) < res.distanceSquaredTo(p)) {
            Node nearNode;
            Node farNode;
            if ((direction % 2 == 0 && p.x() < node.point.x()) || (direction % 2 != 0 && p.y() < node.point.y())) {
                nearNode = node.left;
                farNode = node.right;
            } else {
                nearNode = node.right;
                farNode = node.left;
            }
            res = nearestPoint(nearNode, p, res, direction + 1);
            res = nearestPoint(farNode, p, res, direction + 1);
        }
        return res;
    }
}
