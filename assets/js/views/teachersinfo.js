window.TeachersInfoView = Backbone.View.extend({
  events: {},
  initialize: function() {},

  render: function() {
    $(this.el).html(this.template(this.model.toJSON()));
    console.log(this.model.get('nome') );
    return this;
  }
});
