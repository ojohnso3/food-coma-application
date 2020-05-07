<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/html5bp.css">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
    <link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet'>
  </head>
  <body>
    <script src="../js/jquery-2.1.1.js"></script>
    <script src="../js/jquery-3.1.1.js"></script>
    <script src="https://developer.edamam.com/attribution/badge.js"></script>
    
    <#--  <div class="border" id="left"></div>
    <div class="border" id="right"></div>  -->

    <div class="navigation_bar">   
      <a href="/home" class="bar_item">Home</a>   
      <a href="/login" id="login_header" class="bar_item">Join</a>
      <#--  <img src="../css/foodCOMA.png" alt="Logo" id="real_logo">  -->
      <a href="/home" class="bar_item"><img src="../css/foodCOMA.png" alt="Logo" id="real_logo"></a>
      <a href="/search" class="bar_item">Search</a>
      <a href="/about" class="bar_item">About</a>   
      <#--  <a href="/signup" class="bar_item">Sign Up</a>     -->
    </div>
    
    <script src="../js/global.js"></script>

    ${content}

    <footer class="footer">
      <#--  <a href="https://www.youtube.com/watch?v=astISOttCQ0" target="_blank">Press me!</a>  -->
      <br>
      <a target="_blank" href="https://www.facebook.com/bonappetitmag/"><i class="fa fa-facebook-official"></i></a>
      <a target="_blank" href="https://www.instagram.com/bonappetitmag/?hl=en"><i class="fa fa-instagram"></i></a>
      <a target="_blank" href="https://twitter.com/bonappetit?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"><i class="fa fa-twitter"></i></a>   
      <a target="_blank" href="https://www.linkedin.com/company/bon-appetit/"><i class="fa fa-linkedin"></i></a>
      <a target="_blank" href="https://www.pinterest.com/bonappetitmag/"><i class="fa fa-pinterest-p"></i></a>   
      <p>Sponsored by <a href="http://cs.brown.edu/courses/cs0320/" target="_blank">Brown Computer Science</a></p>
      <div id="edamam-badge" data-color="white"></div>
      <br>
    </footer>
    <#--  <div id="bottom_border"></div>  -->

    <script src="../js/main.js"></script>

     <!-- Again, we're serving up the unminified source for clarity. -->

  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>