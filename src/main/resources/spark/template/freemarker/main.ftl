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
  </head>
  <body>
    <script src="../js/jquery-2.1.1.js"></script>
    <script src="../js/jquery-3.1.1.js"></script>
    
    <div class="navigation_bar">   
      <a href="/home" class="bar_item">Home</a>   
      <a href="/setup" class="bar_item">Login</a>   
      <a href="/foodCOMA" class="bar_item">Search</a>  
      <a href="/about" class="bar_item">About</a>   
    </div>

    ${content}

    <footer class="footer">
      <p>FOOOTER BABY</p>
      <a href="#"><i>Up</i></a>
      <a href="https://www.youtube.com/watch?v=astISOttCQ0" target="_blank">Press me</a>
      <br><br>
      <a href="#"><i class="fa fa-facebook-official"></i></a>
      <a href="#"><i class="fa fa-instagram"></i></a>
      <a href="#"><i class="fa fa-twitter"></i></a>   
      <a href="#"><i class="fa fa-linkedin"></i></a>
      <a href="#"><i class="fa fa-pinterest-p"></i></a>   
      <p>Powered by <a href="https://www.w3schools.com/w3css/default.asp" target="_blank">yo mama</a></p>
    </footer>

     <!-- Again, we're serving up the unminified source for clarity. -->

  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>