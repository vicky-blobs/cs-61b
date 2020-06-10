/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */

public class ColumnMatchFilter extends TableFilter {

    private Table hello;
    private Table.TableRow row;
    private String col1;
    private String col2;

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        hello = input;
        col1 = colName1;
        col2 = colName2;
    }

    @Override
    protected boolean keep() {
        /** No, you access the columns with those names and check their values
         * in each row and make sure they're equal; the keep method should return t/f
         */
        Table.TableRow hola;
        hola = candidateNext();
        int i1 = 1;
        int i2 = 1;
        for (String hi : hello.headerList()) {
            if (hi.equals(col1)) {
                break;
            }
            i1++;
        }
        for (String hi : hello.headerList()) {
            if (hi.equals(col2)) {
                break;
            }
            i2++;
        }
        String one = hola.getValue(i1);
        String two = hola.getValue(i2);
        return one.equals(two);
    }

}
