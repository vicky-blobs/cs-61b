import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Vicky Yu
 */
class ECHashStringSet implements StringSet {

    private int len;
    private LinkedList<String>[] _hold;
    private static double min = 0.2;
    private static double max = 5;

    @SuppressWarnings("unchecked")
    public ECHashStringSet() {
        _hold = (LinkedList<String>[]) new LinkedList[5];
        len = 0;
        for (int i = 0; i < 5; i++) {
            _hold[i] = new LinkedList<String>();
        }
    }

    private double myload() {
        return (double) len / _hold.length;
    }

    @Override
    public void put(String s) {
        if (s != null) {
            if (myload() > max) {
                resize();
            }
            int i = hashy(s);
            if (!_hold[i].contains(s)) {
                _hold[i].add(s);
            }
            len += 1;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int better = _hold.length * 2;
        List listy = asList();
        _hold = new LinkedList[better];
        for (int i = 0; i < _hold.length; i++) {
            _hold[i] = new LinkedList<String>();
        }
        for (Object each: listy) {
            String toadd = (String) each;
            int i = hashy(toadd);
            _hold[i].add(toadd);
        }
    }

    private int hashy(String stringy) {
        return (stringy.hashCode() & 0x7fffffff) % _hold.length;
    }

    @Override
    public boolean contains(String s) {
        if (s != null) {
            return _hold[hashy(s)].contains(s);
        }
        return false;
    }

    @Override
    public List<String> asList() {
        LinkedList<String> listy = new LinkedList<String>();
        for (LinkedList<String> toadd : _hold) {
            if (toadd != null) {
                listy.addAll(toadd);
            }
        }
        return listy;
    }
}
