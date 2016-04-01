window.StudentsInfoView = Backbone.View.extend({
  events: {},
  initialize: function() {},

  render: function() {
    $(this.el).html(this.template(this.model.toJSON()));
    return this;
  }
});
