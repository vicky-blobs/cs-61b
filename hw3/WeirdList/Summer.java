public class Summer implements IntUnaryFunction {
    private int result = 0;
    public Summer(int start) {
        this.result = start;
    }
    public int apply(int next) {
        this.result += next;
        return next;
    }
    public int result() {
        return result;
    }
}
