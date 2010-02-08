<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
  <head>
    <title>${title}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="generator" content="Dajoo Release 2006-05-21" />
    <link rel="stylesheet" media="screen" type="text/css" href="../style.css" />
    <link rel="stylesheet" media="print" type="text/css" href="../print.css" />
    <link rel="shortcut icon" href="../images/favicon.ico" />

    <script language="JavaScript" type="text/javascript">
      var alertText   = 'Please enter the text you want to format.\nIt will be appended to the end of the document.';
      var notSavedYet = 'There are unsaved changes, that will be lost.\nReally continue?';
      var baseURL     = '../';
    </script>
    <script language="JavaScript" type="text/javascript" src="../script.js"></script>

    <!--[if gte IE 5]>
    <style type="text/css">      img { behavior: url("../pngbehavior.htc"); }</style>
    <![endif]-->

  </head>
  <body>
    <div class="toolbar">
    <#list toolbar as button>
      <a href="${button.target}"><img src="${button.image}" alt="${button.alt}" /></a><br/>
    </#list>
    </div>
    <div class="all">

      <div class="header">
        <div class="pagename">
          [[<a href="${path}" onclick="return svchk()" onkeypress="return svchk()">${title}</a>]]
        </div>
        <div class="logo">
          <a href="start" name="top" accesskey="h" title="[ALT+H]" onclick="return svchk()" onkeypress="return svchk()">Dajoo</a>
        </div>
      </div>

      <div class="trace">Trace:
      <#list traces as entry>
        <a href="${entry.path}">${entry.title}</a>&nbsp;
      </#list>
      </div>

	   <div class="topbar">
        <div class="context">Context:Main Reference</div>
	     <div class="action">Action:<a href="${path}">Show</a> <b>Edit</b></div>
	   </div>
      <div class="page">
      <!-- wikipage start -->
<p>
Edit the page and hit <code>Save</code>. See <a href="doku.php?id=wiki:syntax" class="wikilink2" title="wiki:syntax" onclick="return svchk()" onkeypress="return svchk()">syntax</a> for
Wiki syntax. Please edit the page only if you can <strong>improve</strong> it. If you
want to test some things, learn to make your first steps on the
<a href="doku.php?id=wiki:playground" class="wikilink2" title="wiki:playground" onclick="return svchk()" onkeypress="return svchk()">playground</a>.
</p>
<!-- no cachefile used, but created -->
  <form name="editform" method="post" action="${path}?save" accept-charset="utf-8" onsubmit="return svchk()">
  <table style="width:99%">
    <tr>
      <td class="buttons" colspan="3">
      <script language="JavaScript" type="text/javascript">
          textChanged = false;

          formatButton('../images/bold.png','Bold Text','\'\'\'','\'\'\'','Bold Text','b');
          formatButton('../images/italic.png','Italic Text','\'\'','\'\'','Italic Text','i');
          formatButton('../images/underline.png','Underlined Text','__','__','Underlined Text','u');
          formatButton('../images/code.png','Code Text',' ',' ','Code Text','c');

          formatButton('../images/fonth1.png','Level 1 Headline','==','==\n','Level 1 Headline','1');
          formatButton('../images/fonth2.png','Level 2 Headline','===','===\n','Level 2 Headline','2');
          formatButton('../images/fonth3.png','Level 3 Headline','==== ',' ====\n','Level 3 Headline','3');
          formatButton('../images/fonth4.png','Level 4 Headline','=====','=====\n','Level 4 Headline','4');
          formatButton('../images/fonth5.png','Level 5 Headline','======','======\n','Level 5 Headline','5');

          formatButton('../images/link.png','Internal Link','[[',']]','Internal Link','l');
          formatButton('../images/extlink.png','External Link','[',']','http://www.example.com|External Link');

          formatButton('../images/list.png','Ordered List Item',' #','\n','Ordered List Item');
          formatButton('../images/list_ul.png','Unordered List Item','  * ','\n','Unordered List Item');

          insertButton('../images/rule.png','Horizontal Rule','----\n');
          mediaButton('../images/image.png','Add Images and other files','m','');

      </script>
      </td>
    </tr>
    <tr>
      <td colspan="3">
<textarea name="wikitext" id="wikitext"  cols="80" rows="10" class="edit" onchange="textChanged = true;" onkeyup="summaryCheck();" tabindex="1">
${wikiText}
</textarea>
      </td>
    </tr>
    <tr>
      <td>
        <input class="button" type="submit" name="do" value="Save" accesskey="s" title="[ALT+S]" onclick="textChanged=false" onkeypress="textChanged=false" tabindex="3" />
        <input class="button" type="submit" name="do" value="Preview" accesskey="p" title="[ALT+P]" onclick="textChanged=false" onkeypress="textChanged=false" tabindex="4" />
        <input class="button" type="submit" name="do" value="Cancel" tabindex="5" />
      </td>
      <td>
        Edit summary:
        <input type="text" class="edit" name="summary" id="summary" size="50" onkeyup="summaryCheck();" value="created" tabindex="2" />
      </td>
    </tr>
  </table>
  </form>
  <!-- wikipage stop -->
      </div>
    </div>
  </body>
</html>
