import { useState, useEffect } from 'react';
import useEdit from './useEdit';

function useBookmarkModalData() {
	const { isEditBookmark, editData } = useEdit();

	const [formData, setFormData] = useState<{
		name: string;
		url: string;
		comment: string;
	}>({
		name: '',
		url: '',
		comment: '',
	});

	function resetFormData() {
		setFormData({
			name: '',
			url: '',
			comment: '',
		});
	}

	const formDataHandler = (e: any) => {
		setFormData({ ...formData, [e.target.name]: e.target.value });
	};

	useEffect(() => {
		if (isEditBookmark && editData) {
			const { name, url, comment } = editData ?? {};
			setFormData({ name, url, comment });
		}
	}, [editData, isEditBookmark]);

	return {
		formData,
		formDataHandler,
		resetFormData,
	};
}

export default useBookmarkModalData;
