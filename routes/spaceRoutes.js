const express = require("express");
const {
  getSpaces,
  getSpaceById,
  createSpace,
  deleteSpace,
} = require("../controllers/spaceController");
const { protect } = require("../controllers/authController");
const {
  createUploadMiddleware,
  handleUploadErrors,
} = require("../middleware/uploadMiddleware");

const router = express.Router();

const spaceUpload = createUploadMiddleware({
  destination: "public/images/spaces",
  filePrefix: "space",
  maxSize: 5 * 1024 * 1024, // 5MB
  allowedTypes: ["image/"],
  fieldName: "image",
});

router.use(protect);

router.get("/", getSpaces);
router.get("/:spaceId", getSpaceById);
router.post("/", spaceUpload.single(), createSpace);
router.delete("/:spaceId", deleteSpace);

router.use(handleUploadErrors(["image/"]));

module.exports = router;
