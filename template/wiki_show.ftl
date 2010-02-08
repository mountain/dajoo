<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" dir="ltr">
  <head>
    <title>${title}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="generator" content="Dajoo Release 2006-05-21" />
    <link rel="stylesheet" media="screen" type="text/css" href="/style.css" />
    <link rel="stylesheet" media="print" type="text/css" href="/print.css" />
    <link rel="shortcut icon" href="../images/favicon.ico" />
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
	     <div class="action">Action:<b>Show</b> <a href="${path}?action=edit">Edit</a></div>
	   </div>
      <div class="page">
      <!-- wikipage start -->
        ${text}
      <!-- wikipage stop -->
       </div>
    </div>
  </body>
</html>
