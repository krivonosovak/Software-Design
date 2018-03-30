package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CatTest extends TestCommand{

    @DisplayName("test cat")
    @Test
    void test2() throws BadQuotesException, IOException, InterruptedException {

        runManeger("cat ");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("", outContent.toString());

        runManeger("cat test2.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("1\n2 3\n4 5 6\n", outContent.toString());

        runManeger("pwd | cat");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals(System.getProperty("user.dir") + "\n", outContent.toString());

        runManeger("pwd | cat test2.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("1\n2 3\n4 5 6\n", outContent.toString());

        runManeger("cat test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals( "select b.starttime, f.name from\n" +
                "cd.bookings b, cd.facilities f where(\n" +
                "  b.starttime >= '2012-09-21' and b.starttime < '2012-09-22' and f.name like '%Tennis Court%'\n" +
                "  ) order by b.starttime\n" +
                "\n" +
                "\n" +
                "select (a.firstname || ' ' || a.surname) as member, c.name as facility, \n" +
                "(case when a.memid = 0 then b.slots * c.guestcost\n" +
                "     else b.slots * c.membercost\n" +
                "end) as cost from\n" +
                "cd.members a inner join cd.bookings b on (a.memid = b.memid)\n" +
                "inner join cd.facilities c on (b.facid = c.facid)\n" +
                "where ((a.memid = 0 and b.slots * c.guestcost > 30) or (a.memid > 0 and b.slots * c.membercost > 30))\n" +
                "and b.starttime >= '2012-09-14' and b.starttime < '2012-09-15'\n" +
                "order by member\n" +
                "\n" +
                "yo from, hi\n" +
                "\n" +
                "select facid, EXTRACT(MONTH FROM starttime), sum(slots) as \"Total Slots\"\n" +
                "from cd.bookings group by facid, month(starttime) order by facid, month(starttime)\n" +
                "\n" +
                "88007750000\n" +
                "\n" +
                "select d.GENERATE_SERIES as date, b.joindate\n" +
                "from (\n" +
                "  select GENERATE_SERIES \n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n" +
                "inner join cd.members b on extract(month from d.GENERATE_SERIES) = extract(month from b.joindate)\n" +
                "\n" +
                "it my example  from  metro\n" +
                "\n" +
                "select d.GENERATE_SERIES as date, (\n" +
                "  select count(*) from cd.members b\n" +
                "  where extract(month from d.GENERATE_SERIES) = extract(month from b.joindate))\n" +
                "from (\n" +
                "  select GENERATE_SERIES \n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n", outContent.toString());


        runManeger("cat test.txt test2.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("select b.starttime, f.name from\n" +
                "cd.bookings b, cd.facilities f where(\n" +
                "  b.starttime >= '2012-09-21' and b.starttime < '2012-09-22' and f.name like '%Tennis Court%'\n" +
                "  ) order by b.starttime\n" +
                "\n" +
                "\n" +
                "select (a.firstname || ' ' || a.surname) as member, c.name as facility, \n" +
                "(case when a.memid = 0 then b.slots * c.guestcost\n" +
                "     else b.slots * c.membercost\n" +
                "end) as cost from\n" +
                "cd.members a inner join cd.bookings b on (a.memid = b.memid)\n" +
                "inner join cd.facilities c on (b.facid = c.facid)\n" +
                "where ((a.memid = 0 and b.slots * c.guestcost > 30) or (a.memid > 0 and b.slots * c.membercost > 30))\n" +
                "and b.starttime >= '2012-09-14' and b.starttime < '2012-09-15'\n" +
                "order by member\n" +
                "\n" +
                "yo from, hi\n" +
                "\n" +
                "select facid, EXTRACT(MONTH FROM starttime), sum(slots) as \"Total Slots\"\n" +
                "from cd.bookings group by facid, month(starttime) order by facid, month(starttime)\n" +
                "\n" +
                "88007750000\n" +
                "\n" +
                "select d.GENERATE_SERIES as date, b.joindate\n" +
                "from (\n" +
                "  select GENERATE_SERIES \n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n" +
                "inner join cd.members b on extract(month from d.GENERATE_SERIES) = extract(month from b.joindate)\n" +
                "\n" +
                "it my example  from  metro\n" +
                "\n" +
                "select d.GENERATE_SERIES as date, (\n" +
                "  select count(*) from cd.members b\n" +
                "  where extract(month from d.GENERATE_SERIES) = extract(month from b.joindate))\n" +
                "from (\n" +
                "  select GENERATE_SERIES \n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n" +
                "1\n" +
                "2 3\n" +
                "4 5 6\n", outContent.toString());
    }


}
