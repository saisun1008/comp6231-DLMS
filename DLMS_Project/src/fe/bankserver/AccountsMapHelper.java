package fe.bankserver;

/** 
 * Helper class for : AccountsMap
 *  
 * @author OpenORB Compiler
 */ 
public class AccountsMapHelper
{
    private static final boolean HAS_OPENORB;
    static
    {
        boolean hasOpenORB = false;
        try
        {
            Thread.currentThread().getContextClassLoader().loadClass( "org.openorb.orb.core.Any" );
            hasOpenORB = true;
        }
        catch ( ClassNotFoundException ex )
        {
            // do nothing
        }
        HAS_OPENORB = hasOpenORB;
    }
    /**
     * Insert AccountsMap into an any
     * @param a an any
     * @param t AccountsMap value
     */
    public static void insert(org.omg.CORBA.Any a, fe.bankserver.CustomerAccount[] t)
    {
        a.insert_Streamable(new fe.bankserver.AccountsMapHolder(t));
    }

    /**
     * Extract AccountsMap from an any
     *
     * @param a an any
     * @return the extracted AccountsMap value
     */
    public static fe.bankserver.CustomerAccount[] extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        if ( HAS_OPENORB && a instanceof org.openorb.orb.core.Any )
        {
            // streamable extraction. The jdk stubs incorrectly define the Any stub
            org.openorb.orb.core.Any any = ( org.openorb.orb.core.Any ) a;
            try
            {
                org.omg.CORBA.portable.Streamable s = any.extract_Streamable();
                if ( s instanceof fe.bankserver.AccountsMapHolder )
                {
                    return ( ( fe.bankserver.AccountsMapHolder ) s ).value;
                }
            }
            catch ( org.omg.CORBA.BAD_INV_ORDER ex )
            {
            }
            fe.bankserver.AccountsMapHolder h = new fe.bankserver.AccountsMapHolder( read( a.create_input_stream() ) );
            a.insert_Streamable( h );
            return h.value;
        }
        return read( a.create_input_stream() );
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the AccountsMap TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_alias_tc( id(), "AccountsMap", orb.create_sequence_tc( 0, fe.bankserver.CustomerAccountHelper.type() ) );
        }
        return _tc;
    }

    /**
     * Return the AccountsMap IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:fe.bankserver/AccountsMap:1.0";

    /**
     * Read AccountsMap from a marshalled stream
     * @param istream the input stream
     * @return the readed AccountsMap value
     */
    public static fe.bankserver.CustomerAccount[] read(org.omg.CORBA.portable.InputStream istream)
    {
        fe.bankserver.CustomerAccount[] new_one;
        {
        int size7 = istream.read_ulong();
        new_one = new fe.bankserver.CustomerAccount[size7];
        for (int i7=0; i7<new_one.length; i7++)
         {
            new_one[i7] = fe.bankserver.CustomerAccountHelper.read(istream);

         }
        }

        return new_one;
    }

    /**
     * Write AccountsMap into a marshalled stream
     * @param ostream the output stream
     * @param value AccountsMap value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, fe.bankserver.CustomerAccount[] value)
    {
        ostream.write_ulong( value.length );
        for ( int i7 = 0; i7 < value.length; i7++ )
        {
            fe.bankserver.CustomerAccountHelper.write( ostream, value[ i7 ] );

        }
    }

}
