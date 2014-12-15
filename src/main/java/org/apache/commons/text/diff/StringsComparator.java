package org.apache.commons.text.diff;



public class StringsComparator {
    
    private final String left;
    private final String right;
    
    /** Temporary variables. */
    private final int[] vDown;
    private final int[] vUp;
    
    public StringsComparator(String left, String right) {
        this.left = left;
        this.right = right;
        
        final int size = left.length() + right.length() + 2;
        vDown = new int[size];
        vUp   = new int[size];
    }
    
    /**
     * Get the {@link EditScript} object.
     * <p>
     * It is guaranteed that the objects embedded in the {@link InsertCommand
     * insert commands} come from the second sequence and that the objects
     * embedded in either the {@link DeleteCommand delete commands} or
     * {@link KeepCommand keep commands} come from the first sequence. This can
     * be important if subclassing is used for some elements in the first
     * sequence and the <code>equals</code> method is specialized.
     *
     * @return the edit script resulting from the comparison of the two
     *         sequences
     */
    public EditScript getScript() {
        final EditScript script = new EditScript();
        buildScript(0, left.length(), 0, right.length(), script);
        return script;
    }

    /**
     * Build an edit script.
     *
     * @param start1  the begin of the first sequence to be compared
     * @param end1  the end of the first sequence to be compared
     * @param start2  the begin of the second sequence to be compared
     * @param end2  the end of the second sequence to be compared
     * @param script the edited script
     */
    private void buildScript(final int start1, final int end1, final int start2, final int end2,
            final EditScript script) {
        final Snake middle = getMiddleSnake(start1, end1, start2, end2);

        if (middle == null
                || middle.getStart() == end1 && middle.getDiag() == end1 - end2
                || middle.getEnd() == start1 && middle.getDiag() == start1 - start2) {

            int i = start1;
            int j = start2;
            while (i < end1 || j < end2) {
                if (i < end1 && j < end2 && left.charAt(i) == right.charAt(j)) {
                    script.append(new KeepCommand(left.charAt(i)));
                    ++i;
                    ++j;
                } else {
                    if (end1 - start1 > end2 - start2) {
                        script.append(new DeleteCommand(left.charAt(i)));
                        ++i;
                    } else {
                        script.append(new InsertCommand(right.charAt(j)));
                        ++j;
                    }
                }
            }

        } else {

            buildScript(start1, middle.getStart(),
                        start2, middle.getStart() - middle.getDiag(),
                        script);
            for (int i = middle.getStart(); i < middle.getEnd(); ++i) {
                script.append(new KeepCommand(left.charAt(i)));
            }
            buildScript(middle.getEnd(), end1,
                        middle.getEnd() - middle.getDiag(), end2,
                        script);
        }
    }
    
    private Snake getMiddleSnake(int start1, int end1, int start2, int end2) {
     // Myers Algorithm
        // Initialisations
        final int m = end1 - start1;
        final int n = end2 - start2;
        if (m == 0 || n == 0) {
            return null;
        }

        final int delta  = m - n;
        final int sum    = n + m;
        final int offset = (sum % 2 == 0 ? sum : sum + 1) / 2;
        vDown[1+offset] = start1;
        vUp[1+offset]   = end1 + 1;

        for (int d = 0; d <= offset ; ++d) {
            // Down
            for (int k = -d; k <= d; k += 2) {
                // First step

                final int i = k + offset;
                if (k == -d || k != d && vDown[i-1] < vDown[i+1]) {
                    vDown[i] = vDown[i+1];
                } else {
                    vDown[i] = vDown[i-1] + 1;
                }

                int x = vDown[i];
                int y = x - start1 + start2 - k;

                while (x < end1 && y < end2 && left.charAt(x) == right.charAt(y)) {
                    vDown[i] = ++x;
                    ++y;
                }
                // Second step
                if (delta % 2 != 0 && delta - d <= k && k <= delta + d) {
                    if (vUp[i-delta] <= vDown[i]) {
                        return buildSnake(vUp[i-delta], k + start1 - start2, end1, end2);
                    }
                }
            }

            // Up
            for (int k = delta - d; k <= delta + d; k += 2) {
                // First step
                final int i = k + offset - delta;
                if (k == delta - d
                        || k != delta + d && vUp[i+1] <= vUp[i-1]) {
                    vUp[i] = vUp[i+1] - 1;
                } else {
                    vUp[i] = vUp[i-1];
                }

                int x = vUp[i] - 1;
                int y = x - start1 + start2 - k;
                while (x >= start1 && y >= start2
                        && left.charAt(x) == right.charAt(y)) {
                    vUp[i] = x--;
                    y--;
                }
                // Second step
                if (delta % 2 == 0 && -d <= k && k <= d ) {
                    if (vUp[i] <= vDown[i + delta]) {
                        return buildSnake(vUp[i], k + start1 - start2, end1, end2);
                    }
                }
            }
        }

        // this should not happen
        throw new RuntimeException("Internal Error");
    }
    
    /**
     * Build a snake.
     *
     * @param start  the value of the start of the snake
     * @param diag  the value of the diagonal of the snake
     * @param end1  the value of the end of the first sequence to be compared
     * @param end2  the value of the end of the second sequence to be compared
     * @return the snake built
     */
    private Snake buildSnake(final int start, final int diag, final int end1, final int end2) {
        int end = start;
        while (end - diag < end2
                && end < end1
                && left.charAt(end) == right.charAt(end - diag)) {
            ++end;
        }
        return new Snake(start, end, diag);
    }

    /**
     * This class is a simple placeholder to hold the end part of a path
     * under construction in a {@link StringsComparator StringsComparator}.
     */
    private static class Snake {

        /** Start index. */
        private final int start;

        /** End index. */
        private final int end;

        /** Diagonal number. */
        private final int diag;

        /**
         * Simple constructor. Creates a new instance of Snake with specified indices.
         *
         * @param start  start index of the snake
         * @param end  end index of the snake
         * @param diag  diagonal number
         */
        public Snake(final int start, final int end, final int diag) {
            this.start = start;
            this.end   = end;
            this.diag  = diag;
        }

        /**
         * Get the start index of the snake.
         *
         * @return start index of the snake
         */
        public int getStart() {
            return start;
        }

        /**
         * Get the end index of the snake.
         *
         * @return end index of the snake
         */
        public int getEnd() {
            return end;
        }

        /**
         * Get the diagonal number of the snake.
         *
         * @return diagonal number of the snake
         */
        public int getDiag() {
            return diag;
        }
    }
    
    public static void main(String[] args) {
        StringsComparator sc = new StringsComparator("O Bruno eh um bom rapaz. Ele eh do Brasil.", "O Bruno foi um bom rapaz. Ele eh do Brasil .");
        EditScript es = sc.getScript();
        es.visit(new CommandVisitor() {
            
            boolean nlAdd = true;
            boolean nlRemove = true;

            @Override
            public void visitInsertCommand(Object object) {
                if (nlAdd) {
                    System.out.println();
                    System.out.print("> ");
                    nlAdd = false;
                }
                System.out.print(object);
            }

            @Override
            public void visitKeepCommand(Object object) {
                if (!nlAdd) {
                    nlAdd = true;
                }
                if (!nlRemove) {
                    nlRemove = true;
                    System.out.println();
                }
                System.out.print(object);
            }

            @Override
            public void visitDeleteCommand(Object object) {
                if (nlRemove) {
                    System.out.println();
                    System.out.print("< ");
                    nlRemove = false;
                }
                System.out.print(object);
            }
        });
    }

}
