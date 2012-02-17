/^TRUNCATE TABLE \(\w\+\);$/{
    s//DELETE FROM \1/
    b
}
/^INSERT INTO.*)$/{
    h
    d
}
/^INSERT INTO/,/^VALUES$/{
    /^INSERT INTO/{
        h
        d
    }
    /^VALUES$/!H
}
/^VALUES$/,/;$/{
    /^VALUES$/!{
        x
        s/\n/ /g
        x
        s/[,;]$//
        G
        s/^\(.*\)\n\(.*\)$/\2 VALUES \1/
        s/\s\s*/ /g
        b
    }
}
d
