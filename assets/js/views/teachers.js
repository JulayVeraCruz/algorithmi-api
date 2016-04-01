window.TeachersView = Backbone.View.extend({
  events: {},
  initialize: function() {},

  render: function() {
    var self = this;

    $(this.el).html(this.template());

    modem('GET', 'teachers', function(data) {
      $('#subcontent', self.el).html(JSON.stringify(data));
    }, function(error) {
      console.log('Error getting teacher list!');
    });

    return this;
  }
});
