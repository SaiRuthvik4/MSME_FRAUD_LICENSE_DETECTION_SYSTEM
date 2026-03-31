import React, { useState } from "react";
import { motion } from "framer-motion";
import api from "../services/api"; // ✅ use your configured axios instance

const AddApplicant = () => {
  const [form, setForm] = useState({
    name: "",
    mobile: "",
    businessName: "",
    panNumber: "",
    aadhaarNumber: "",
    gstNumber: "",
    aadhaarFile: null,
    panFile: null,
  });

  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    if (e.target.files) {
      setForm({ ...form, [e.target.name]: e.target.files[0] });
    } else {
      setForm({ ...form, [e.target.name]: e.target.value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    const data = new FormData();
    Object.keys(form).forEach((key) => data.append(key, form[key]));

    try {
      const res = await api.post("/api/applicants/register", data, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      setMessage(
        `Applicant ${res.data.name} added successfully! Fraud Score: ${res.data.fraudScore}`
      );

      setForm({
        name: "",
        mobile: "",
        businessName: "",
        panNumber: "",
        aadhaarNumber: "",
        gstNumber: "",
        aadhaarFile: null,
        panFile: null,
      });
    } catch (err) {
      setMessage(err.response?.data?.message || "Error submitting applicant");
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.div
      className="form-container"
      initial={{ opacity: 0, y: -50 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.7 }}
    >
      <h2>Add Applicant</h2>

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={form.name}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="mobile"
          placeholder="Mobile"
          value={form.mobile}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="businessName"
          placeholder="Business Name"
          value={form.businessName}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="panNumber"
          placeholder="PAN Number"
          value={form.panNumber}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="aadhaarNumber"
          placeholder="Aadhaar Number"
          value={form.aadhaarNumber}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="gstNumber"
          placeholder="GST Number"
          value={form.gstNumber}
          onChange={handleChange}
          required
        />

        <input
          type="file"
          name="aadhaarFile"
          accept="image/*,application/pdf"
          onChange={handleChange}
          required
        />

        <input
          type="file"
          name="panFile"
          accept="image/*,application/pdf"
          onChange={handleChange}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? "Submitting..." : "Submit"}
        </button>
      </form>

      {message && <p>{message}</p>}
    </motion.div>
  );
};

export default AddApplicant;