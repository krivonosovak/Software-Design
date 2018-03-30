package Commands;

import exceptions.BadQuotesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GrepTest extends TestCommand{

    @DisplayName("test grep")
    @Test
    void test() throws IOException, BadQuotesException, InterruptedException {
        runManeger("echo 123 | grep 1 ");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("123\n", outContent.toString());


        runManeger("grep 1 test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("  b.starttime >= '2012-09-21' and b.starttime < '2012-09-22' and f.name like '%Tennis Court%'\n" +
                "and b.starttime >= '2012-09-14' and b.starttime < '2012-09-15'\n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n", outContent.toString());


        runManeger("grep -i SeLEcT test.txt ");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("select b.starttime, f.name from\n" +
                "select (a.firstname || ' ' || a.surname) as member, c.name as facility, \n" +
                "select facid, EXTRACT(MONTH FROM starttime), sum(slots) as \"Total Slots\"\n" +
                "select d.GENERATE_SERIES as date, b.joindate\n" +
                "  select GENERATE_SERIES \n" +
                "select d.GENERATE_SERIES as date, (\n" +
                "  select count(*) from cd.members b\n" +
                "  select GENERATE_SERIES \n", outContent.toString());


        runManeger("grep -w \"fro\" test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("", outContent.toString());


        runManeger("grep -w \"from \" test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("from (\n" +
                "it my example  from  metro\n" +
                "from (\n", outContent.toString());


        runManeger("grep -A 2 1  test.txt ");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("  b.starttime >= '2012-09-21' and b.starttime < '2012-09-22' and f.name like '%Tennis Court%'\n" +
                "  ) order by b.starttime\n" +
                "\n" +
                "and b.starttime >= '2012-09-14' and b.starttime < '2012-09-15'\n" +
                "order by member\n" +
                "\n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n" +
                "inner join cd.members b on extract(month from d.GENERATE_SERIES) = extract(month from b.joindate)\n" +
                "\n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n", outContent.toString());


        runManeger("grep -w \"from \" test.txt test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("test.txt:from (\n" +
                "test.txt:it my example  from  metro\n" +
                "test.txt:from (\n" +
                "test.txt:from (\n" +
                "test.txt:it my example  from  metro\n" +
                "test.txt:from (\n", outContent.toString());

        runManeger("grep -wiA1 \"froM\" test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("select b.starttime, f.name from\n" +
                "cd.bookings b, cd.facilities f where(\n" +
                "end) as cost from\n" +
                "cd.members a inner join cd.bookings b on (a.memid = b.memid)\n" +
                "yo from, hi\n" +
                "\n" +
                "select facid, EXTRACT(MONTH FROM starttime), sum(slots) as \"Total Slots\"\n" +
                "from cd.bookings group by facid, month(starttime) order by facid, month(starttime)\n" +
                "\n" +
                "from (\n" +
                "  select GENERATE_SERIES \n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n" +
                "inner join cd.members b on extract(month from d.GENERATE_SERIES) = extract(month from b.joindate)\n" +
                "\n" +
                "it my example  from  metro\n" +
                "\n" +
                "  select count(*) from cd.members b\n" +
                "  where extract(month from d.GENERATE_SERIES) = extract(month from b.joindate))\n" +
                "from (\n" +
                "  select GENERATE_SERIES \n" +
                "  from GENERATE_SERIES('2012-08-01'::date,'2012-08-31','1 day')) d\n", outContent.toString());
    }

    @DisplayName("test grep exception")
    @Test
    void test2() throws IOException, BadQuotesException, InterruptedException {
        runManeger("grep reg sdfsdfs");
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("grep: sdfsdfs: No such file or directory\n", outContent.toString());

        runManeger("grep");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("usage: grep [-A <num>] [-i] [-w]\n" +
                " -A,--after-context <num>    Print num lines of trailing context after each\n" +
                "                             match. See also the -B and -C options.\n" +
                " -i,--ignore-case            Perform case insensitive matching. By default, grep\n" +
                "                             is case sensitive.\n" +
                " -w,--word-regexp            The expression is searched for as a word (as if\n" +
                "                             surrounded by `[[:<:]]' and `[[:>:]]'; see\n" +
                "                             re_format(7)).\n", outContent.toString());

        runManeger("grep -d from test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("usage: grep [-A <num>] [-i] [-w]\n" +
                " -A,--after-context <num>    Print num lines of trailing context after each\n" +
                "                             match. See also the -B and -C options.\n" +
                " -i,--ignore-case            Perform case insensitive matching. By default, grep\n" +
                "                             is case sensitive.\n" +
                " -w,--word-regexp            The expression is searched for as a word (as if\n" +
                "                             surrounded by `[[:<:]]' and `[[:>:]]'; see\n" +
                "                             re_format(7)).\n", outContent.toString());

        runManeger("grep -A r from test.txt");
        outContent.reset();
        t = m.getPrintThread();
        t.join(mills);
        assertEquals("grep: Invalid argument\n", outContent.toString());
    }
}