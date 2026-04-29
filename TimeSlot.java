public class TimeSlot {
    int start, end;
    Node room;
    String subject;

    public TimeSlot(int s, int e, Node r, String sub) {
        start = s;
        end = e;
        room = r;
        subject = sub;
    }

    public String toString() {
        int sh = start / 60;
        int sm = start % 60;
        int eh = end / 60;
        int em = end % 60;

        return subject + " (" +
                String.format("%02d:%02d-%02d:%02d", sh, sm, eh, em) +
                ") @ " + room;
    }
}
