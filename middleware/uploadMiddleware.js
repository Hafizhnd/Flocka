const multer = require("multer");
const path = require("path");
const fs = require("fs");

const createUploadMiddleware = (options = {}) => {
  const {
    destination = "public/images", // Default destination
    filePrefix = "file", // Default file prefix
    maxSize = 5 * 1024 * 1024, // Default 5MB
    allowedTypes = ["image/"], // Default allow images only
    fieldName = "image", // Default field name
  } = options;

  const uploadDir = path.join(__dirname, `../${destination}`);
  if (!fs.existsSync(uploadDir)) {
    fs.mkdirSync(uploadDir, { recursive: true });
  }

  const storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, uploadDir);
    },
    filename: function (req, file, cb) {
      const uniqueSuffix = Date.now() + "-" + Math.round(Math.random() * 1e9);
      const fileExtension = path.extname(file.originalname);
      cb(null, `${filePrefix}-${uniqueSuffix}${fileExtension}`);
    },
  });

  const fileFilter = (req, file, cb) => {
    const isAllowed = allowedTypes.some((type) =>
      file.mimetype.startsWith(type)
    );
    if (isAllowed) {
      cb(null, true);
    } else {
      const allowedTypesStr = allowedTypes.join(", ").replace(/\//g, "");
      cb(new Error(`Only ${allowedTypesStr} files are allowed!`), false);
    }
  };

  const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
    limits: {
      fileSize: maxSize,
    },
  });

  return {
    single: (field = fieldName) => upload.single(field),
    multiple: (field = fieldName, maxCount = 5) =>
      upload.array(field, maxCount),
    fields: (fields) => upload.fields(fields),
  };
};

const handleUploadErrors = (allowedTypes = ["image/"]) => {
  return (error, req, res, next) => {
    if (error instanceof multer.MulterError) {
      if (error.code === "LIMIT_FILE_SIZE") {
        return res.status(400).json({
          success: false,
          message: "File upload failed",
          error: "File size too large. Check the maximum allowed size",
        });
      }
      if (error.code === "LIMIT_FILE_COUNT") {
        return res.status(400).json({
          success: false,
          message: "File upload failed",
          error: "Too many files uploaded",
        });
      }
      if (error.code === "LIMIT_UNEXPECTED_FILE") {
        return res.status(400).json({
          success: false,
          message: "File upload failed",
          error: "Unexpected file field",
        });
      }
    }

    if (error.message.includes("files are allowed!")) {
      return res.status(400).json({
        success: false,
        message: "File upload failed",
        error: error.message,
      });
    }

    next(error);
  };
};

const imageUpload = createUploadMiddleware({
  destination: "public/images",
  filePrefix: "image",
  maxSize: 5 * 1024 * 1024,
  allowedTypes: ["image/"],
  fieldName: "image",
});

module.exports = {
  createUploadMiddleware,
  handleUploadErrors,
  imageUpload,
};
