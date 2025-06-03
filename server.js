const app = require('./app');
require('dotenv').config();

const PORT = process.env.PORT || 5000;

app.listen(PORT, '0.0.0.0', () => {
  console.log(`🚀 Server is running on http://localhost:${PORT}`);

  console.log("-------------------------------------------------------");
  console.log("Node.js Current Time:", new Date().toString());
  console.log("Node.js Timezone Offset (minutes from UTC):", new Date().getTimezoneOffset());
  console.log("-------------------------------------------------------");
});