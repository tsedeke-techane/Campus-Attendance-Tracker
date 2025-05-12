import express from "express";
import { protect } from "../middleware/authMiddleware.js";
import Class from "../models/class.js";
const router = express.Router();

router.use(protect);

router.get("/Dashboard", async (req, res) => {
  try {
    if (req.user.role === "student") {
      // Find classes where the student is enrolled
      const classes = await Class.find({ students: req.user.id }).populate("teacher");
      return res.status(200).json({
        success: true,
        count: classes.length,
        data: classes,
      });
    }

    if (req.user.role === "teacher") {
      // Find classes where the user is the teacher
      const classes = await Class.find({ teacher: req.user.id }).populate("students");
      return res.status(200).json({
        success: true,
        count: classes.length,
        data: classes,
      });
    }

    res.status(403).json({
      success: false,
      message: "Unauthorized role",
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message,
    });
  }
});

export default router;
