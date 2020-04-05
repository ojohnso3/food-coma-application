
<#assign content>
    <style>
    
    body {background-image: url('https://1.bp.blogspot.com/-ZfFeWXSXIFs/UuUwd7h263I/AAAAAAAAFrY/LpLrzPKVcHs/s1600/black+n+white+wallpaper+Leonardo+DiCaprio.jpg');
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
    background-attachment: fixed;
    }
    
    </style>
<header> <codeHead>
<div style="text-align:center">
  <p><strong><h3><font color = "blue">${welcoming}</font></h3></strong></p>
</div></codeHead></header>
<div style="text-align:center">
<section> <code>
<#list actors as act>
  <li><h3><a href="/actor/${act.encoded}">${act.name}</a>
</h3></li>
</#list>
</code>
</section>
</div>

</#assign>

<#include "mainDb.ftl">