package com.jinjiang.wxc.parser;
	import java.util.Locale;
	 
	import org.htmlparser.Node;
	import org.htmlparser.NodeFilter;
	import org.htmlparser.Tag;
	 
	/***
	36
	 * This class accepts all tags matching the tag name.
	37
	 */
	public class TagNameFilter
	    implements
	        NodeFilter
	{
	    /***
	43
	     * The tag name to match.
	44
	     */
	    protected String mName;
	 
	    /***
	48
	     * Creates a new instance of TagNameFilter.
	49
	     * With no name, this would always return <code>false</code>
	50
	     * from {@link #accept}.
	51
	     */
	    public TagNameFilter ()
	    {
	        this ("");
	    }
	 
	    /***
	58
	     * Creates a TagNameFilter that accepts tags with the given name.
	59
	     * @param name The tag name to match.
	60
	     */
	    public TagNameFilter (String name)
	    {
	        mName = name.toUpperCase (Locale.ENGLISH);
	    }
	 
	    /***
	67
	     * Get the tag name.
	68
	     * @return Returns the name of acceptable tags.
	     */
	    public String getName ()
	    {
	        return (mName);
	    }
	 
	    /***
	76
	     * Set the tag name.
	77
	     * @param name The name of the tag to accept.
	78
	     */
	    public void setName (String name)
	    {
	        mName = name;
	    }
	 
	    /***
	85
	     * Accept nodes that are tags and have a matching tag name.
	86
	     * This discards non-tag nodes and end tags.
	87
	     * The end tags are available on the enclosing non-end tag.
	88
	     * @param node The node to check.
	89
	     * @return <code>true</code> if the tag name matches,
	90
	     * <code>false</code> otherwise.
	91
	     */
	
	    public boolean accept (Node node)
	
	    {
	
	        return ((node instanceof Tag)
	
	                && !((Tag)node).isEndTag ()
	
	                && ((Tag)node).getTagName ().equals (mName));
	
	    }
	
	}

