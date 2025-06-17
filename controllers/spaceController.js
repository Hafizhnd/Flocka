const Space = require("../models/Space");
const path = require("path");
const fs = require("fs");

exports.getSpaces = async (req, res) => {
  try {
    const spaces = await Space.getAll();
    res.json({
      success: true,
      data: spaces,
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "An error occurred while fetching spaces",
      error: error.message,
    });
  }
};

exports.getSpaceById = async (req, res) => {
  try {
    const { spaceId } = req.params;
    const space = await Space.getById(spaceId);

    if (!space) {
      return res.status(404).json({
        success: false,
        message: "Space not found",
      });
    }

    res.json({
      success: true,
      data: space,
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "An error occurred while fetching the space",
      error: error.message,
    });
  }
};

exports.createSpace = async (req, res) => {
  try {
    const spaceData = { ...req.body };

    if (req.file) {
      spaceData.image = `/public/images/spaces/${req.file.filename}`;
    }

    const newSpace = await Space.create(spaceData);

    res.status(201).json({
      success: true,
      message: "Space created successfully",
      data: newSpace,
    });
  } catch (error) {
    if (req.file) {
      const filePath = path.join(
        __dirname,
        "../public/images/spaces",
        req.file.filename
      );
      fs.unlink(filePath, (unlinkError) => {
        if (unlinkError) {
          console.error("Error deleting uploaded file:", unlinkError);
        }
      });
    }

    res.status(500).json({
      success: false,
      message: "An error occurred while creating the space",
      error: error.message,
    });
  }
};

exports.deleteSpace = async (req, res) => {
  try {
    const { spaceId } = req.params;
    const deletedSpace = await Space.delete(spaceId);

    if (!deletedSpace) {
      return res.status(404).json({
        success: false,
        message: "Space not found",
      });
    }

    if (deletedSpace.image) {
      const imagePath = deletedSpace.image.replace("/public", "");
      const filePath = path.join(__dirname, "..", "public", imagePath);

      fs.unlink(filePath, (unlinkError) => {
        if (unlinkError && unlinkError.code !== "ENOENT") {
          console.error("Error deleting space image:", unlinkError);
        }
      });
    }

    res.json({
      success: true,
      message: "Space deleted successfully",
      data: deletedSpace,
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: "An error occurred while deleting the space",
      error: error.message,
    });
  }
};
