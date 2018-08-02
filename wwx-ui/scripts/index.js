var wwxIndex = {};
wwxIndex.init = function () {
  wwxIndex.loadFoucsImg("foucsSlideList");
  wwxIndex.SearchList("#i_q_keyword_1", ".t_ajaxbox", "#searchListBtn", "/hz/list/suggest");
  var scale = 600 / 1920;
  $(window).resize(function () {
    var w = $(window).width();
    if (w < 1200) {
      w = 1200
    }
    var h = w * scale;
    $("#foucsSlideList,#foucsSlideList li,#foucsSlideList li img,.foucsSlide").css({
      "width": w + "px"
    });
    $("#foucsSlideList,#foucsSlideList li,#foucsSlideList li img,.foucsSlide").css({
      "height": h + "px"
    })
  });
  var w = $(window).width();
  if (w < 1200) {
    w = 1200
  }
  var h = w * scale;
  $("#foucsSlideList,#foucsSlideList li,#foucsSlideList li img,.foucsSlide").css({
    "width": w + "px"
  });
  $("#foucsSlideList,#foucsSlideList li,#foucsSlideList li img,.foucsSlide").css({
    "height": h + "px"
  })
};
wwxIndex.SearchList = function (input, box, button, url) {
  var step = -1;
  var oldValue = "";
  var listTimer = null;
  var oInput = $(input),
    oBox = $(box),
    oUl = oBox.find("ul"),
    aLi = oUl.find("li"),
    oButton = $(button);
  $(document).click(function () {
    oBox.hide()
  });
  oInput.keyup(function (ev) {
    var oEvent = ev || event;
    var keyCode = oEvent.keyCode;
    var oTxt = $.trim(oInput.val());
    if (oTxt == "") {
      return
    }
    if (oEvent.keyCode == 38 || oEvent.keyCode == 40) {
      return
    }
    if (oEvent.keyCode == 13) {
      $(button).click()
    }
    clearInterval(listTimer);
    listTimer = setTimeout(function () {
      $.ajax({
        type: "GET",
        dataType: "json",
        url: url,
        data: {
          qwd: oTxt
        },
        success: function (json) {
          var msg = json.data;
          if (msg.length == 0) {
            oBox.hide();
            return false
          }
          var ulstr = "";
          oUl.html(ulstr);
          for (var i = 0; i < msg.length; i++) {
            if (!msg[i].name) {
              oBox.hide();
              return
            }
            var goUrl = ZRCONFIG.URL_LIST;
            var txt = "";
            ulstr += '<li data="' + goUrl + '"><span class="keyword" style="margin-right:5px;">' + msg[i].name +
              "</span>" + msg[i].subname;
            ulstr += txt
          }
          oUl.html(ulstr).end().show()
        }
      })
    }, 500);
    oldValue = oTxt;
    step = -1
  });
  oInput.keydown(function (ev) {
    var oEvent = ev || event;
    var keyCode = oEvent.keyCode;
    if (oEvent.keyCode == 40) {
      var aLi = oBox.find("ul").find("li");
      step++;
      if (step == aLi.length) {
        step = -1
      }
      aLi.removeClass("keyhover");
      if (step != -1) {
        aLi.eq(step).addClass("keyhover");
        oInput.attr("_url", aLi.eq(step).attr("data"));
        oInput.val(aLi.eq(step).find(".keyword").text())
      } else {
        oInput.val(oldValue);
        oInput.attr("_url", "")
      }
    } else {
      if (oEvent.keyCode == 38) {
        var aLi = oBox.find("ul").find("li");
        step--;
        if (step == -2) {
          step = aLi.length - 1
        }
        aLi.removeClass("keyhover");
        if (step != -1) {
          aLi.eq(step).addClass("keyhover");
          oInput.attr("_url", aLi.eq(step).attr("data"));
          oInput.val(aLi.eq(step).find(".keyword").text())
        } else {
          oInput.val(oldValue);
          oInput.attr("_url", "")
        }
      }
    } if (oEvent.keyCode == 38 || oEvent.keyCode == 40) {
      return false
    }
  });
  oBox.find("ul li").live("click", function () {
    oInput.val($(this).find(".keyword").text());
    toList()
  });
  aLi.live("mouseover mouseout", function (event) {
    aLi.removeClass("keyhover");
    if (event.type == "mouseover") {
      $(this).addClass("keyhover");
      oInput.attr("_url", $(this).attr("data"));
      step = $(this).index()
    } else {
      $(this).removeClass("keyhover");
      oInput.attr("_url", "");
      step = $(this).index()
    }
  });
  $(button).click(function () {
    toList()
  });
  oInput.keydown(function (e) {
    if (e.keyCode == 13) {
      toList()
    }
  });

  function toList() {
    var b = $.trim($("#i_q_keyword_1").val()),
      d = $("#searchTxt").attr("data-val");
    if (d == 1) {
      window.location.href = ZRCONFIG.ZZ_URL_LIST + "?qwd=" + encodeURI(b)
    } else {
      window.location.href = ZRCONFIG.URL_LIST + "?qwd=" + encodeURI(b)
    }
  }
};
wwxIndex.loadFoucsImg = function (obj) {
  var ID = $("#" + obj);
  var aImg = ID.find("img");
  var tmp = [];
  var iLoaded = 0;
  var n = 0;
  for (var i = 0; i < aImg.size(); i++) {
    tmp.push(aImg.eq(i).attr("_src"))
  }
  aImg.eq(0).attr("src", tmp[0]);
  var timer = setInterval(function () {
    n++;
    if (n > aImg.size() - 1) {
      clearInterval(timer)
    }
    aImg.eq(n).attr("src", tmp[n])
  }, 500);
  jQuery(".foucsSlide").slide({
    mainCell: ".bd ul",
    effect: "fold",
    autoPlay: true,
    interTime: 5000
  })
};
$(function () {
  //fixedApp();
  wwxIndex.init();
  jQuery(".bigSlide").slide({
    mainCell: ".bd ul",
    effect: "fold",
    autoPlay: !0,
    interTime: 5000
  }), jQuery(".houseSlide").slide({
    mainCell: ".bd ul",
    effect: "fold",
    autoPlay: !0,
    interTime: 5000
  }), jQuery(".smallSlide").slide({
    mainCell: ".bd ul",
    effect: "fold",
    autoPlay: !0,
    interTime: 5000
  }), $(".categorys dl").each(function () {
    var a = $(this).find("dd").find(".detail");
    a.each(function () {
      $(this).find("a").size() < 2 && $(this).hide()
    })
  }), $("#videoBtn").click(function () {
    new Boxy("#videoBox", {
      title: $(this).attr("data-title")
    })
  });
  $("#searchType span").click(function () {
    $("#searchTxt").html($(this).html() + "<b></b>");
    $("#searchType").hide();
    $("#searchTxt").attr("data-val", $(this).attr("data-val"))
  });
  $("#searchList").hover(function () {
    $("#searchType").show()
  }, function () {
    $("#searchType").hide()
  });
  setTimeout(function () {
    slideLeft()
  }, 2000)
});

function slideLeft() {
  var oSlideLeftBar = $("#slideLeftBar");
  var oSlideBar = $("#slideBar");
  var leftTop = oSlideLeftBar.offset().top;
  var rightTop = oSlideBar.offset().top;
  var tmp = [];
  $(".bigtit").each(function () {
    tmp.push($(this).offset().top)
  });
  $("#slideLeftBar li").click(function () {
    var index = $(this).index();
    var top = tmp[index] + 1;
    $(window).scrollTop(top)
  });
  $(window).scroll(function () {
    var winTop = $(window).scrollTop();
    setActive(winTop)
  });
  setActive();

  function setActive(winTop) {
    leftTop = oSlideLeftBar.offset().top;
    if (winTop > 640) {
      oSlideLeftBar.show().css({
        "position": "fixed",
        "top": "10px"
      })
    } else {
      oSlideLeftBar.hide().css({
        "position": "absolute",
        "top": "640px"
      })
    } if (winTop >= parseInt(tmp[0])) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(0) a").addClass("active")
    }
    if (winTop >= tmp[1]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(1) a").addClass("active")
    }
    if (winTop >= tmp[2]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(2) a").addClass("active")
    }
    if (winTop >= tmp[3]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(3) a").addClass("active")
    }
    if (winTop >= tmp[4]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(4) a").addClass("active")
    }
    if (winTop >= tmp[5]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(5) a").addClass("active")
    }
    if (winTop >= tmp[6]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(6) a").addClass("active")
    }
    if (winTop >= tmp[7]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(7) a").addClass("active")
    }
    if (winTop >= tmp[8]) {
      oSlideLeftBar.find("li a").removeClass("active");
      oSlideLeftBar.find("li:eq(8) a").addClass("active")
    }
  }
}
