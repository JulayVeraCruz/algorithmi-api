Backbone.View.prototype.close = function() {
  this.remove();
  this.unbind();
  this.undelegateEvents();
};

var Router = Backbone.Router.extend({
  currentView: undefined,
  showView: function(view, elem, sub) {
    elem.show();

    if (sub == false) {
      if (this.currentView)
        this.currentView.close();

      this.currentView = view;
      this.currentView.delegateEvents();
    }
    var rendered = view.render();
    elem.html(rendered.el);
  },
  routes: {
    "teachers": "teachers",
    "teachers/new": "teachersNew",
    "teachers/:id": "teachersInfo",
    "students": "students",
    "students/new": "studentsNew",
    "students/:id": "studentsInfo",
    "schools": "schools",
    "schools/new": "schoolsNew",
    "schools/:id": "schoolsInfo",
    "tests": "tests",
    "tests/new": "testsNew",
    "tests/:id": "testsInfo",
    "submissions": "submissions",
    "submissions/:id": "submissionsInfo",

    "login": "login",
    "": "index"
  },

  index: function() {
    //validar o login
    app.navigate("/login", {
      trigger: true
    });
  },
  login: function() {
    var login = new LoginView();
    $('#header').html("");
    $('#footer').html("");
    $('#content').html(login.render().el);
  },

  teachers: function() {
    var self = this;

    templateLoader.load(["TeachersView"],
      function() {
        var v = new TeachersView({});
        self.showView(v, $('#content'));
      }
    );
  },
  teachersNew: function() {
    var self = this;

    templateLoader.load(["TeachersNewView"],
      function() {
        var v = new TeachersNewView({});
        self.showView(v, $('#content'));
      }
    );
  },
  teachersInfo: function(id) {
    var self = this;

    templateLoader.load(["TeachersInfoView"],
      function() {
        var ss = new Teacher({
          id: id
        });
        ss.fetch(function() {
          var v = new TeachersInfoView({
            model: ss
          });
          self.showView(v, $('#content'));
        });
      }
    );
  },

  students: function() {
    var self = this;

    templateLoader.load(["StudentsView"],
      function() {
        var v = new StudentsView({});
        self.showView(v, $('#content'));
      }
    );
  },
  studentsNew: function() {
    var self = this;

    templateLoader.load(["StudentsNewView"],
      function() {
        var v = new StudentsNewView({});
        self.showView(v, $('#content'));
      }
    );
  },
  studentsInfo: function(id) {
    var self = this;

    templateLoader.load(["StudentsInfoView"],
    function() {
      var ss = new Student({
        id: id
      });
      ss.fetch().done(function () {
        var v = new StudentsInfoView({
          model: ss
        });
        self.showView(v, $('#content'));
      });
    }
    );
  },

  schools: function() {
    var self = this;

    templateLoader.load(["SchoolsView"],
      function() {
        var v = new SchoolsView({});
        self.showView(v, $('#content'));
      }
    );
  },
  schoolsNew: function() {
    var self = this;

    templateLoader.load(["SchoolsNewView"],
      function() {
        var v = new SchoolsNewView({});
        self.showView(v, $('#content'));
      }
    );
  },
  schoolsInfo: function(id) {
    var self = this;

    templateLoader.load(["SchoolsInfoView"],
      function() {
        var v = new SchoolsInfoView({});
        self.showView(v, $('#content'));
      }
    );
  },

  tests: function() {
    var self = this;

    templateLoader.load(["TestsView"],
      function() {
        var v = new TestsView({});
        self.showView(v, $('#content'));
      }
    );
  },
  testsNew: function() {
    var self = this;

    templateLoader.load(["TestsNewView"],
      function() {
        var v = new TestsNewView({});
        self.showView(v, $('#content'));
      }
    );
  },
  testsInfo: function(id) {
    var self = this;

    templateLoader.load(["TestsInfoView"],
      function() {
        var v = new TestsInfoView({});
        self.showView(v, $('#content'));
      }
    );
  },

  submissions: function() {
    var self = this;

    templateLoader.load(["SubmissionsView"],
      function() {
        var v = new SubmissionsView({});
        self.showView(v, $('#content'));
      }
    );
  },
  submissionsInfo: function(id) {
    var self = this;

    templateLoader.load(["SubmissionsInfoView"],
      function() {
        var v = new SubmissionsInfoView({});
        self.showView(v, $('#content'));
      }
    );
  },
});


templateLoader.load(["LoginView"],
  function() {
    app = new Router();
    Backbone.history.start();
  }
);
