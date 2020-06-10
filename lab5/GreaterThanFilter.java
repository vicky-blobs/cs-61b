/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    private Table hello;
    private String col;
    private String matchy;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        hello = input;
        col = colName;
        matchy = ref;
    }

    @Override
    protected boolean keep() {
        Table.TableRow hola;
        hola = candidateNext();
        int i = 0;
        for (String hi : headerList()) {
            if (hi.equals(col)) {
                break;
            }
            i++;
        }
        String ho = hola.getValue(i);
        return ho.compareTo(matchy) > 0;
    }

}
