/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    private Table hello;
    private Table.TableRow row;
    private String col;
    private String matchy;

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        hello = input;
        col = colName;
        matchy = match;

    }

    @Override
    protected boolean keep() {
        Table.TableRow hola;
        hola = candidateNext();
        int i = 0;
        for (String hi: headerList()) {
            if (hi.equals(col)) {
                break;
            }
            i++;
        }
        String hey;
        hey = hola.getValue(i);
        return hey.equals(matchy);
    }

}
