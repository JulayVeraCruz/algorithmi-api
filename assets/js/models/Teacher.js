var Teacher = Backbone.Model.extend({
  initialize: function (options) {
    this.id = options.id;
  },
  fetch: function (after_fetch) {
    var self = this;

    modem('GET', 'user/' + this.id,
      function (json) {
        self.set("username", json.username);
        self.set("escola", json.escola);

        after_fetch();
      },
      function (xhr, ajaxOptions, thrownError) {
        var json = JSON.parse(xhr.responseText);
        error_launch(json.message);
      }
    );
  }
});
