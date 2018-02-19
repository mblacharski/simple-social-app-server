

var getURLParameter = function(paramName){
  var url = window.location.search.substring(1);
  var sURLVariables = url.split('&');
  for (var i = 0; i < sURLVariables.length; i++)
	    {
	        var sParameterName = sURLVariables[i].split('=');
	        if (sParameterName[0] == paramName)
	        {
	            return sParameterName[1];
	        }
	    }

}


$(document).ready(function(){;
  var name = getURLParameter('username');
  var socket = new WebSocket("ws://localhost:3000/wall/" + name);
  socket.onmessage = function (e) {
    var req = JSON.parse(e.data);
    console.log(e);
    switch (req.eventCode) {
      case "AU":
        console.log(e);
        $("#activeUsers").html("");
        let users = req.users;
        for(let i = 0; i < users.length; i++){
              var btn = $('<input />', {
                 type  : 'button',
                 value : 'Follow ' + users[i],
                 class    : 'follow',
                 "data-ref" : users[i],
                 "data-sub" : true,
                 on    : {
                    click: function() {
                        follow($(this));
                    }
                 }
             });
            $("#activeUsers").append('<p>' + users[i]).append(btn).append('</p>');
        }
        break;
      case "PO" :
        $("#posts").html("");
        var posts = req.posts;
        for(let i = 0; i<posts.length; i++){
          $("#posts").append(
            '<div class="post">'
            + '<p class="date">'
            + formatDate(posts[i].timestamp)
            + "</p>"
            +'<h5>'
            + posts[i].user
            + '</h5>'
            + "<p>"
            + posts[i].content
            + "</p></div>"
          );
        }
        break;
      case "OP" :
        $("#myPosts").html("");
        var posts = req.posts;
        for(let i = 0; i<posts.length; i++){
          $("#myPosts").append(
            '<div class="post">'
            + '<p class="date">'
            + formatDate(posts[i].timestamp)
            + "</p>"
            + "<p>"
            + posts[i].content
            + "</p></div>"
          );
        }
        break;
      default:
        return;
    }
  }

  $("#addNewPost").click(function(){
    let req = {
      "eventCode" : "NP",
      "content" : $("#newPostArea").val(),
      "timestamp" : new Date(),
      "user" : getURLParameter('username')
    }
    console.log("New post request " + JSON.stringify(req));
    socket.send(JSON.stringify(req));
  });
  var follow = function(button){
    let sub = $(button).data("sub");
    let followed = $(button).data("ref");
    let req = {
      "eventCode" : sub ? "FO" : "UF",
      "user" : getURLParameter('username'),
      "followed" : followed
    };
    sub = !sub;
    $(button).data('sub', sub);
    $(button).prop('value', (sub ? "Follow " : "Unfollow ") + followed);
    $("#posts").html("");
    console.log(JSON.stringify(req));
    socket.send(JSON.stringify(req));
  }
  var formatDate = function(date) {
    let d = new Date(date);
    var curr_day = d.getDate();
    var curr_month = d.getMonth();
    var curr_year = d.getFullYear();

    var curr_hour = d.getHours();
    var curr_min = d.getMinutes();
    var curr_sec = d.getSeconds();

    curr_month++ ; // In js, first month is 0, not 1

    return ("dnia" + curr_day + "." + curr_month + "." + curr_year) + " o " +
      curr_hour + ":" + curr_min + ":" + curr_sec;
  };
});
