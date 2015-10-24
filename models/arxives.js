var mongoose = require('mongoose');

var ArxivSchema = new mongoose.Schema({
  name: String,
  path: String,
  tags: Array,
  date: Date
  //tags: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Tag' }]
});

mongoose.model('Arxiv', ArxivSchema);

/*
var TagsSchema = new mongoose.Schema({
  value: String,
  arxives: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Arxiv' }]
});

mongoose.model('Tag', TagsSchema);
*/